package Config

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection, MongoCredential, ServerAddress}
import Schema.{ReviewLikedDisLikedData, Reviews, ReviewsData, ReviewsLikesDisLikes}

import scala.jdk.CollectionConverters.SeqHasAsJava

trait MongoDBConfig extends Serializable{
  def initialiseMongodb(): MongoClient = {
    val user: String = "mongo-user" // the user name
    val database: String = "admin" // the name of the database in which the user is defined
    val password: Array[Char] = "mongo-pw".toCharArray // the password as a character array
    val credential1: MongoCredential = MongoCredential.createCredential (user, database, password)
    val mongodbClient: MongoClient = MongoClient (
      MongoClientSettings.builder ()
        .applyToClusterSettings ((builder: ClusterSettings.Builder) => builder.hosts (List (new ServerAddress ("localhost:27017") ).asJava) )
        .credential (credential1)
        .build () )
    return mongodbClient;
  }

  def getReviewsCollection (mongodbClient :MongoClient) :MongoCollection[ReviewsData] = {
    val codecRegistry = fromRegistries(fromProviders(classOf[ReviewsData],classOf[Reviews]), DEFAULT_CODEC_REGISTRY )
    val databaseConnection = mongodbClient.getDatabase ("db").withCodecRegistry(codecRegistry)
    databaseConnection.createCollection("movieReviews")
    val reviewCollection: MongoCollection[ReviewsData] = databaseConnection.getCollection[ReviewsData] ("movieReviews");
    return reviewCollection
  }


  def getReviewLikedDisLikedCollection (mongodbClient :MongoClient) :MongoCollection[ReviewsLikesDisLikes] = {
    val codecRegistry = fromRegistries(fromProviders(classOf[ReviewLikedDisLikedData],classOf[ReviewsLikesDisLikes]), DEFAULT_CODEC_REGISTRY )
    val databaseConnection = mongodbClient.getDatabase ("db").withCodecRegistry(codecRegistry)
    databaseConnection.createCollection("movieReviewsLikedDisLikedData")
    val reviewsLikedDisLikedCollection: MongoCollection[ReviewsLikesDisLikes] = databaseConnection.getCollection[ReviewsLikesDisLikes] ("movieReviews");
    return reviewsLikedDisLikedCollection
  }
}
