import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.{Seconds, StreamingContext}



/**

 * Create by lyz on 2019/5/20

 */

object StateStreamingApp {



  def main(args: Array[String]): Unit = {

    val ssc = new StreamingContext("local[*]", "StatStreamingApp", Seconds(5))

    val kafkaParams = Map[String, Object](

      "bootstrap.servers" -> "192.168.13.135:9092",

      "key.deserializer" -> classOf[StringDeserializer],

      "value.deserializer" -> classOf[StringDeserializer],

      "group.id" -> "example",

      "auto.offset.reset" -> "latest",

      "enable.auto.commit" -> (false: java.lang.Boolean)

    )

    val topics = List("moercredit_log_test").toSet  //flumeTopic

    val stream = KafkaUtils.createDirectStream[String, String](

      ssc,

      PreferConsistent,

      Subscribe[String, String](topics, kafkaParams)

    ).map(_.value())

    stream.print()

    ssc.start()

    ssc.awaitTermination();

  }

}

