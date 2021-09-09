/*


import java.nio.file.Files
import java.util.Properties
import java.util.concurrent.Future

import kafka.server.{KafkaConfig, KafkaServerStartable}
import org.apache.curator.test.TestingServer
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.{BeforeAndAfterAll, PrivateMethodTester}

import scala.collection.JavaConverters._


class Kafkatest  extends AnyFunSpec with BeforeAndAfterAll with PrivateMethodTester{

  val testLogsDir = "kafka-test-logs"
  val kafkaPort: Int = 9092
  val stringSerializerName = "org.apache.kafka.common.serialization.StringSerializer"
  val stringDeserializerName = "org.apache.kafka.common.serialization.StringDeserializer"
  val kafkaHost: String = "localhost"


  //creating Zookeeper
  val zkServer = new TestingServer()

  //setting the Broker Configurations
  val brokerProps = new Properties()
  brokerProps.put("broker.id", "0")
  brokerProps.put("log.dirs", Files.createTempDirectory(testLogsDir).toAbsolutePath.toString)
  brokerProps.put("host.name", kafkaHost)
  brokerProps.put("zookeeper.connect", zkServer.getConnectString)
  brokerProps.put("port", kafkaPort.toString)
  // brokerProps.put("auto.create.topics.enable", "true")



  //Kafka Server Configurations
  val config: KafkaConfig = new KafkaConfig(brokerProps)
  val kafkaServer: KafkaServerStartable = new KafkaServerStartable(config)


  override def beforeAll: Unit ={
    kafkaServer.startup()
    println("starting server in my code")
  }

  override def afterAll: Unit = {
    //kafkaProducer.close()
    kafkaServer.shutdown()
    zkServer.close()
    zkServer.stop()
  }

  describe("kafka example"){
    it("send example"){


      val kafkaClientConf: java.util.Map[String, Object] = Map(
        "bootstrap.servers"  -> s"$kafkaHost:$kafkaPort",
        "auto.offset.reset"  -> "earliest",
        "key.serializer"     -> stringSerializerName,
        "value.serializer"   -> stringSerializerName,
        "key.deserializer"   -> stringDeserializerName,
        "value.deserializer" -> stringDeserializerName,
        "group.id"           -> "kafkaexample"
      ).asInstanceOf[Map[String, Object]].asJava

      def kafkaProducer: KafkaProducer[String, String] = new KafkaProducer[String, String](kafkaClientConf)
      //def kafkaConsumer: KafkaConsumer[String,String] = new KafkaConsumer[String, String](kafkaClientConf)


      val producerRecord: ProducerRecord[String, String] = new ProducerRecord[String, String]("top", "1", "data")

      val p: Future[RecordMetadata] =kafkaProducer.send(producerRecord)
      println("val of p:"+p.get().topic())
      kafkaProducer.flush()
      kafkaProducer.close()
    }
  }
}






*/
