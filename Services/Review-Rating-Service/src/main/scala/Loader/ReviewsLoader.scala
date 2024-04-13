package Loader

import Config.MongoDBConfig
import Schema.ReviewsData
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt




object ReviewsLoader extends App with MongoDBConfig{
  val mongodbClient = initialiseMongodb();
  val reviewsCollection = getReviewsCollection(mongodbClient);
  val json = jawn.decode[Seq[ReviewsData]](os.read(os.pwd/"src"/"main"/"scala"/"loader"/"JsonFiles"/"reviews.json").toString)
  json.fold((error) => println(error),(lst:Seq[ReviewsData]) => {
    lst.distinct.foreach(elem => {
      try {
        val result = reviewsCollection.insertOne(elem).toFuture();
        var result1 = Await.result(result, 10.seconds);
        println(result1)
      } catch {
        case e: Exception => {
          println(e.getMessage,"skks")
        }
      }
    })
   });
//  val result = reviewsCollection.drop().toFuture();
//  result.onComplete {
//    case Success(resultData) => {
//      println("Done")
//    }
//    case Failure(t) => println("An error has occurred: " + t.getMessage)
//  }


}