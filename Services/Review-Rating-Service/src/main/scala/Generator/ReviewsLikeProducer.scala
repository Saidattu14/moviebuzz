package Generator

import Config.KafkaConfig
import Schema.{ReviewLikedDisLikedMsg, ReviewLikedDisLikedPayload, ReviewsData}
import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import io.circe.jawn
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import upickle.default._

import scala.util.Random
import scala.util.control.Breaks.break

object ReviewsLikeProducer extends App with KafkaConfig{
  implicit val ownerRw2: ReadWriter[ReviewLikedDisLikedPayload] = macroRW
  implicit val ownerRw1: ReadWriter[ReviewLikedDisLikedMsg] = macroRW
  val json = jawn.decode[Seq[ReviewsData]](os.read(os.pwd/"src"/"main"/"scala"/"loader"/"JsonFiles"/"reviews.json").toString)
    val js = json.getOrElse(Seq[ReviewsData] ())
    if(js.nonEmpty) {
      val producer: KafkaProducer[String, String] = getKafkaProducer;
      var cnt = 0;
      while (true) {
        val lt = js.length;
        val alphabet = 'a' to 'z'
        val id = Random.between(0,lt);
        val rId = Random.between(0,js(id).reviews.length)
        val nm = Random.nextInt(alphabet.size)
        val userId = alphabet(nm);
       val reviewsProduceMsgPayload = ReviewLikedDisLikedPayload(js(id).ImdbId,js(id).reviews(rId).reviewId,isLiked = true,isDisLiked = false,Random.nextInt().toString)
      // val reviewsProduceMsgPayload = ReviewLikedDisLikedPayload("tt0898704","rw2777241",isLiked = true,isDisLiked = false,Random.nextInt().toString)
        val msg = ReviewLikedDisLikedMsg("LikedOrDisLikedInformation", reviewsProduceMsgPayload)
        val json: String = write(msg)
        val data = new ProducerRecord[String, String]("ReviewLikedOrDisLikedTopic", null, json)
        producer.send(data)
        println(json)
        cnt = cnt + 1
        if(cnt == 100000) {
          break
        }
        println(cnt)
      }
    }
}
