package Config

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer

import java.util.Properties
import scala.jdk.CollectionConverters.SeqHasAsJava

trait KafkaConfig {
  def getConsumerKafkaProps: Properties = {
    val props: Properties = new Properties ();
    props.put ("group.id", "kafka-consumer-Reviews-Rating")
    props.put ("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094")
    props.put ("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put ("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put ("enable.auto.commit", "true")
    props.put ("auto.commit.interval.ms", "1000")
    return props
  }


  def getProducerKafkaProps: Properties = {
    val props: Properties = new Properties ();
    props.put ("group.id", "kafka-Producer-Reviews-Rating")
    props.put ("bootstrap.servers", "localhost:9092,localhost:9093")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("acks","all")

    return props
  }




  def getKafkaConsumer : KafkaConsumer[String,GenericRecord] = {
    val consumer = new KafkaConsumer[String,GenericRecord] (getConsumerKafkaProps);
    val topics = List ("RequestReviewsTopic")
    consumer.subscribe (topics.asJava)
    return consumer
  }

  def getKafkaProducer : KafkaProducer[String,String] = {
    val producer = new KafkaProducer[String, String](getProducerKafkaProps)
    return producer
  }

}