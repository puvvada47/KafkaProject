package com.github.viswanath.kafka.example

import java.time.Duration
import java.util.{Properties, _}

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters._

object BasicKafkaConsumerASyncManualCommit {
  private val logger: Logger = LoggerFactory.getLogger(BasicKafkaConsumerAutoCommit.getClass.getName)

  def main(args: Array[String]): Unit = {
    val topic: String = "data"

    val properties: Properties = new Properties

    //Kafka consumer Configurations
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "consumerApplication")

    //create the Kafka Consumer
    val kafkaConsumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](properties)

    //Subscribing to Topic
    kafkaConsumer.subscribe(Collections.singletonList(topic))

    //Polling for new data
    while (true) {
      val consumerRecords: ConsumerRecords[String, String] = kafkaConsumer.poll(Duration.ofMillis(10))
      for (consumerRecord <- consumerRecords.asScala) {
        logger.info("Key: " + consumerRecord.key + ", Value:" + consumerRecord.value)
        logger.info("Partition:" + consumerRecord.partition + ",Offset:" + consumerRecord.offset)
        println(consumerRecord.key()+":"+consumerRecord.value()+":"+consumerRecord.partition()+":"+consumerRecord.offset())
        kafkaConsumer.commitAsync()
      }
    }
  }
}





/*
committing into kafka should be done after processing the messages i.e;saving into db
*/
