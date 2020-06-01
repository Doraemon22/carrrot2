import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
 * Spark Streaming整合Kafka
 *
 * @author zhiying.dong@hand-china.com 2019/05/24 16:54
 */
object KafkaDirect{
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("DirectKafka")
      .setMaster("local[2]")

    val ssc = new StreamingContext(conf, Seconds(5))

    val topicsSet = Array("moercredit_log_test")
    val kafkaParams = mutable.HashMap[String, String]()
    //必须添加以下参数，否则会报错
    kafkaParams.put("bootstrap.servers", "192.168.13.135:9092")
    kafkaParams.put("group.id", "atguigu")
    kafkaParams.put("enable.auto.commit","false")
    kafkaParams.put("auto.offset.reset", "earliest")
    kafkaParams.put("partition.assignment.strategy","org.apache.kafka.clients.consumer.RangeAssignor")
    kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    val messages = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](topicsSet, kafkaParams
      )
    )

    // Get the lines, split them into words, count the words and print
    val lines = messages.map(_.value)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L)).reduceByKey(_ + _)
    wordCounts.print()

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}