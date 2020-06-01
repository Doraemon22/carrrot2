/*
import jodd.util.PropertiesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;

public class HBaseConsumer {
    public static void main(String[] args) {
        //加载配置信息
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(PropertiesUtil.properties);
        //消费kafka.topics
        kafkaConsumer.subscribe(Arrays.asList(PropertiesUtil.getProperty("kafka.topics")));

        HbaseDao hd = new HbaseDao();
        while (true) {
            //拉消息过来
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            //遍历你拉的消息
            for (ConsumerRecord<String, String> cr : records) {
                String ori = cr.value();
                System.out.println(ori);
                //17269452013,15542823911,2018-08-28 11:58:23,0800
                hd.put(ori);
            }
        }
    }
}
*/
