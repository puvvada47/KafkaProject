package com.github.viswanath.kafka.example

import java.util.Properties
import java.util.logging.Logger

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

object SynchronousKafkaProducer {

  private val logger = Logger.getLogger(SynchronousKafkaProducer.getClass.getName)

  def main(args: Array[String]): Unit = {

    val topic: String = "data"
    val properties: Properties = new Properties


    //Kafka producer Configurations
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    //create the Kafka Producer
    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](properties)

    for (i <- 1 to 10) {
      val producerRecord: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, Integer.toString(i), "testing msg:" + i)
      val recordMetaData: RecordMetadata = producer.send(producerRecord).get() //(get: sending synchronous data forcefully)
      logger.info("Successfully received the details as: \n" +
        "Topic:" + recordMetaData.topic() + "\n" +
        "Partition: " + recordMetaData.partition() + "\n" +
        "Offset: " + recordMetaData.offset() + "\n" +
        "Timestamp: " + recordMetaData.timestamp())
    }

    //flushing the data( The flush() will force all the data to get produced)
    producer.flush()

    //closing the kafka
    producer.close()
  }
}


//At the end, a get() function is used when the data is sent to the Kafka.
// This method sends the data synchronously and forcefully. The users can try their own ways to implement the keys.