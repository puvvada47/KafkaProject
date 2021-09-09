 import java.time.Duration
 import java.util._
 import org.apache.kafka.clients.consumer._
 import org.apache.kafka.clients.producer._
 import org.apache.kafka.common.serialization.{LongDeserializer, LongSerializer, StringDeserializer, StringSerializer}
 import org.junit._
 import org.scalatest.funspec.AnyFunSpec
 import org.scalatest.{BeforeAndAfterAll, PrivateMethodTester}
 import org.springframework.kafka.test.rule.EmbeddedKafkaRule
 import scala.collection.JavaConverters._


  class embeddedkafkaTest extends AnyFunSpec with BeforeAndAfterAll with PrivateMethodTester{
    val topic = "test_topic"
    private var testConsumer = null

    @ClassRule
   private  val kafka: EmbeddedKafkaRule =new EmbeddedKafkaRule(1,
      false, topic)


    override protected def beforeAll(): Unit = {
      kafka.before()
    }


    override protected def afterAll(): Unit = {
      kafka.getEmbeddedKafka.destroy
    }


  describe("kafka") {
    it("test") {

      val producerProperties = new Properties
      producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getEmbeddedKafka.getBrokersAsString)
      producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[LongSerializer].getName)
      producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
      producerProperties.setProperty(ProducerConfig.ACKS_CONFIG, "1")
      producerProperties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd")
      val producer = new KafkaProducer[Long, String](producerProperties)
      val timestamp = new Date().getTime
      val record = new ProducerRecord[Long, String](topic, timestamp, "message")
      producer.send(record, (recordMetadata: RecordMetadata, e: Exception) => {
        def foo(recordMetadata: RecordMetadata, e: Exception) = {
          System.out.println("message successfully produced!")
          System.out.println("topic: " + recordMetadata.topic)
          System.out.println("offset: " + recordMetadata.offset)
          System.out.println("partition: " + recordMetadata.partition)
          System.out.println("timestamp: " + recordMetadata.timestamp)
        }

        foo(recordMetadata, e)
      })
      producer.close()
      // consume the message produced above
      val consumerProperties = new Properties
      consumerProperties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getEmbeddedKafka.getBrokersAsString)
      consumerProperties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[LongDeserializer].getName)
      consumerProperties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
      consumerProperties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test-group")
      consumerProperties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      val consumer = new KafkaConsumer[Long, String](consumerProperties)
      consumer.subscribe(Collections.singletonList(topic))
      var count = 0

      val records: ConsumerRecords[Long, String] = consumer.poll(Duration.ofMillis(1000))

        for (rec<-records.asScala) {
          System.out.println("message key: " + rec.key)
          System.out.println("message value: " + rec.value)
        }


      Assert.assertEquals(1, records.count)
      Assert.assertEquals("message", records.iterator.next.value)
      Assert.assertEquals(timestamp, records.iterator.next.key)

    }

  }

}


