package com.github.viswanath.kafka.example

import java.util.Properties

import org.apache.kafka.clients.producer._
import org.slf4j.{Logger, LoggerFactory}


object AsynchronousKafkaProducerWithCallBack {


  private val logger: Logger =LoggerFactory.getLogger(AsynchronousKafkaProducerWithCallBack.getClass.getName)

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
     /* producer.send(producerRecord, (recordMetadata: RecordMetadata, e: Exception) => {
        if (e == null) {
          logger.info("Successfully received the details as: \n" +
            "Topic:" + recordMetadata.topic() + "\n" +
            "Partition: " + recordMetadata.partition() + "\n" +
            "Offset: " + recordMetadata.offset() + "\n" +
            "Timestamp: " + recordMetadata.timestamp())
        }
        else {
          logger.error("Can't produce,getting error")
        }
      })*/
    }


    /*

    //alternative way
    for(i<-1 to 10) {
         val producerRecord: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, Integer.toString(i), "testing msg:" + i)
         producer.send(producerRecord,new Callback {
           override def onCompletion(recordMetadata: RecordMetadata, e: Exception) = {

             if (e == null) {
               logger.info("Successfully received the details as: \n" +
                 "Topic:" + recordMetadata.topic() + "\n" +
                 "Partition: " + recordMetadata.partition() + "\n" +
                 "Offset: " + recordMetadata.offset() + "\n" +
                 "Timestamp: " + recordMetadata.timestamp())
             }
             else {
               logger.warning("Can't produce,getting error")

             }

           }
         })

       }
   */
    //flushing the data( The flush() will force all the data to get produced)
    producer.flush()

    //closing the kafka
    producer.close()
  }
}
