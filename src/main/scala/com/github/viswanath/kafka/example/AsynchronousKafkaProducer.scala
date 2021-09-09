package com.github.viswanath.kafka.example

import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

object AsynchronousKafkaProducer {


  def main(args: Array[String]): Unit = {

    val topic: String = "data"
    val key: String = "1"
    val value: String = "viswanath"
    val properties: Properties = new Properties


    //Kafka producer Configurations
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    //create the Kafka Producer
    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](properties)

    //Create the Producer Record
    val producerRecord: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, key, value)

    //Send data Asynchronous
    producer.send(producerRecord)


    //flushing the data( The flush() will force all the data to get produced)
    producer.flush()

    //closing the kafka
    producer.close()
  }
}
