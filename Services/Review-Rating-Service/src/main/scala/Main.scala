import Config.{KafkaConfig, MongoDBConfig}
import Schema._
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.streaming.{GroupState, Trigger}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{ForeachWriter, SparkSession}
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates.{pullByFilter, push, set}
import org.mongodb.scala.model.{Filters, Updates}
import org.mongodb.scala.result.UpdateResult
import org.mongodb.scala.{MongoClient, MongoCollection, Observer, result}
import upickle.default._

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.{Callable, Executors, ScheduledExecutorService, TimeUnit}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.jdk.DurationConverters.ScalaDurationOps
import scala.language.postfixOps
import scala.util.{Failure, Success}



case class MongoDBSink() extends ForeachWriter[Count] with MongoDBConfig {

  var mongodbClient : MongoClient = _;
  var reviewsCollection: MongoCollection[ReviewsData] = _;

  def open(partitionId: Long,version: Long): Boolean = {
    mongodbClient = initialiseMongodb();
    reviewsCollection = getReviewsCollection(mongodbClient);
    true
  }
  def process(value: Count): Unit = {
    reviewsCollection.updateOne(and(equal("ImdbId",value.movieId),equal("reviews.reviewId",value.reviewId)),Updates.combine(set("reviews.$.likesCount",value.likedCount),set("reviews.$.dislikesCount",value.disLikedCount))).subscribe(new Observer[result.UpdateResult] {
      override def onNext(result: UpdateResult): Unit = {}
      override def onError(e: Throwable): Unit = println(e)
      override def onComplete(): Unit = {}
    });
  }

  def close(errorOrNull: Throwable): Unit = {
    mongodbClient.close();
  }
}


case class Average1(eventType:String, payload: Count)

case class Count(var reviewId: String, var likedCount: Long, var disLikedCount: Long,var movieId:String) {
  def updateWith (newMessage: ReviewLikedDisLikedPayload): Unit = {
    movieId = newMessage.movieId;
    if(newMessage.isLiked) {
      likedCount = likedCount + 1;
    } else{
      disLikedCount = disLikedCount + 1;
    }
  }
}

object Main extends KafkaConfig with MongoDBConfig with Serializable{
  def main(args: Array[String]): Unit = {
    implicit val ownerRw1: ReadWriter[Reviews] = macroRW
    implicit val ownerRw2: ReadWriter[ReviewsSentPayload] = macroRW
    implicit val ownerRw3: ReadWriter[ReviewsSentMsg] = macroRW
    implicit val ownerRw4: ReadWriter[ReviewLikedDisLikedPayload] = macroRW
    implicit val ownerRw5: ReadWriter[ReviewLikedDisLikedMsg] = macroRW
    implicit val ownerRw6: ReadWriter[ReviewsData] = macroRW
    implicit val ownerRw7: ReadWriter[TrendingReviewsMsg] = macroRW


    def updateTrendingMovieReviews(reviewsCollection : MongoCollection[ReviewsData],producer :KafkaProducer[String, String]): Unit = {
      val res = reviewsCollection.find().toFuture();
      res.onComplete {
        case Success(resultData) => {
          var lst = List.empty[ReviewsData];
          resultData.foreach(f => {
            val r = f.reviews.sortBy(r => r.likesCount + r.dislikesCount).reverse.slice(0,3);
            val t = ReviewsData(f.ImdbId,f._id,r);
            lst +:= t;
          });
          val msg = TrendingReviewsMsg("trendingMovieReviewsInformation", lst)
          val json: String = write(msg)
          val data = new ProducerRecord[String, String]("ResponseReviewsTopic", null, json)
          producer.send(data);
        }
        case Failure(t) => println("An error has occurred: " + t.getMessage)
      }
    }

    def initialiseKafkaConsumer(): Unit = {
      val mongodbClient = initialiseMongodb();
      val reviewsCollection = getReviewsCollection(mongodbClient);
      val reviewsLikesDisLikesCollection = getReviewLikedDisLikedCollection(mongodbClient);
      val consumer : KafkaConsumer[String,GenericRecord] = getKafkaConsumer;
      val scheduler: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor ();
      val producer: KafkaProducer[String, String] = getKafkaProducer;
      var count: Int = 0;
      scheduler.schedule (new Callable[Unit] {
        override def call (): Unit = {
          while (!scheduler.isShutdown) {
            try {
              Thread.sleep((30 second) toMillis);
              if(count == 100) {
                updateTrendingMovieReviews(reviewsCollection,producer);
                count = 0;
              } else{
                count = count + 1;
              }
              val records: ConsumerRecords[String, GenericRecord] = consumer.poll((60 second) toJava)
              records.iterator().forEachRemaining { record: ConsumerRecord[String, GenericRecord] =>
                if(record.key().equals("getReviewsForMovie")) {
                  val json = jawn.decode[MessageData](record.value().toString)
                  json.fold((error) => println(error), (p) => {
                    val reviewsList = reviewsCollection.find(equal("ImdbId", p.payload.movieId)).toFuture();
                    reviewsList.onComplete {
                      case Success(resultData) => {
                        val reviewsProduceMsgPayload = ReviewsSentPayload(p.payload.movieId, p.payload.requestId, resultData.head.reviews)
                        val msg = ReviewsSentMsg("reviewsInformation", reviewsProduceMsgPayload)
                        val json: String = write(msg)
                        val data = new ProducerRecord[String, String]("ResponseReviewsTopic", null, json)
                        producer.send(data);
                      }
                      case Failure(t) => println("An error has occurred: " + t.getMessage)
                    }
                  })
                } else if(record.key().equals("addReview")) {
                  val json = jawn.decode[AddReviewMsg](record.value().toString)
                  json.fold((error) => println(error), (p) => {
                    val review = Reviews(0,0,p.payload.review,p.payload.rating.toString,LocalDate.now.format(DateTimeFormatter.ofPattern("yyyyMMdd")).toString,p.payload.reviewer_name,"","",p.payload.reviewId);
                    //collection.findOneAndUpdate(equal("ImdbId", p.payload.movieId),push("tours.$.items",review))
                    reviewsCollection.findOneAndUpdate(equal("ImdbId", p.payload.movieId), push("reviews", review)).subscribe(new Observer[ReviewsData] {
                      override def onNext(result: ReviewsData): Unit = {}
                      override def onError(e: Throwable): Unit = println(e)
                      override def onComplete(): Unit = {}
                    });
                  });
                }
                else if(record.key().equals("deleteReview")) {
                  println(record)
                  val json = jawn.decode[DeleteReviewMsg](record.value().toString)
                  json.fold((error) => println(error), (p) => {
                    reviewsCollection.updateOne(equal("ImdbId", p.payload.movieId),pullByFilter(Filters.eq("reviews",and(equal("reviewId",p.payload.reviewId))))).subscribe(new Observer[result.UpdateResult] {
                      override def onNext(result: UpdateResult): Unit = {}
                      override def onError(e: Throwable): Unit = println(e)
                      override def onComplete(): Unit = {}
                    });
                  });
                }
                consumer.commitSync();
              }
            } catch {
              case e: Exception => {
                println(e,"skks")
              }
            }
          }
        }
      }, 0, TimeUnit.SECONDS)

    }

  initialiseKafkaConsumer();
    val definitionSchema = new StructType()
      .add(StructField("eventType", StringType, nullable = true))
      .add(StructField("payload",new StructType()
        .add(StructField("movieId", StringType, nullable = true))
        .add(StructField("reviewId", StringType, nullable = true))
        .add(StructField("userId", StringType, nullable = true))
        .add(StructField("isLiked", BooleanType, nullable = true))
        .add(StructField("isDisLiked", BooleanType, nullable = true))
      ));

    val spark = SparkSession
      .builder()
      .appName("test")
      .master("local")
      .getOrCreate()
    import spark.implicits._

    val ds  = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092,localhost:9093")
      .option("subscribe", "ReviewLikedOrDisLikedTopic")
      .option("failOnDataLoss", "false")
      .option("includeHeaders", "true")
      .load()
      .selectExpr("cast(value as string)")
      .select(from_json(col("value"), definitionSchema).as("data"))
      .select(col("data.payload").as[ReviewLikedDisLikedPayload]);
    ds.printSchema();

    def mappingFunc(key: String, newMessages: Iterator[ReviewLikedDisLikedPayload], state: GroupState[Count]): Count = {
      var cnt = state.getOption.getOrElse {
        Count(key, 0, 0, null)
      }
      newMessages.foreach(msg => {
        cnt.updateWith(msg)
      })
      state.update(cnt)
      cnt
    }
    val counts = ds.groupByKey(msg => msg.reviewId).mapGroupsWithState(mappingFunc _)
    val writer = new MongoDBSink()
    val query = counts.writeStream.foreach(writer).trigger(Trigger.ProcessingTime("25 second")).outputMode("update").option("checkpointLocation", "output/").start();
    query.awaitTermination();
  }
}