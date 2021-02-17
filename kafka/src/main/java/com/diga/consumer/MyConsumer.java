package com.diga.consumer;

import com.diga.generic.utils.PropUtils;
import com.diga.generic.utils.URLUtils;
import com.diga.generic.utils.log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

public class MyConsumer {
    // 加载配置
    private static final Properties properties = PropUtils.get(URLUtils.classpath("kafka-consumer.properties"));
    // 创建消费者
    private static final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
    // 定义我们操作的 TOPIC 的名称
    private static final String topicName = "hello";

    public static void main(String[] args) {
        consumer.subscribe(Collections.singletonList(topicName));

        // 无线监听
        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(1000);

            for (ConsumerRecord<String, String> record : poll) {
                log.info("键:%s, 值:%s", record.key(), record.value());
            }
        }
    }
}
