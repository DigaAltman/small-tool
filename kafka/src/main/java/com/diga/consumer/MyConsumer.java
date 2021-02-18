package com.diga.consumer;

import com.diga.generic.utils.PropUtils;
import com.diga.generic.utils.URLUtils;
import com.diga.generic.utils.log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.NoOpConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class MyConsumer {
    // 加载配置
    private static final Properties properties = PropUtils.get(URLUtils.classpath("kafka-consumer.properties"));
    // 创建消费者
    private static final KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);


    public static void simpleConsumerByTopic() {
        consumer.subscribe(Collections.singletonList("hello"));

        ConsumerRecords<String, String> poll = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : poll) {
            log.info("键:%s, 值:%s", record.key(), record.value());
        }
    }

    /**
     * 基于正则表达式来完成主题的订阅
     */
    public static void patternConsumerByTopic() {
        consumer.subscribe(Pattern.compile("hello*"), new NoOpConsumerRebalanceListener());

        ConsumerRecords<String, String> poll = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : poll) {
            log.info("键:%s, 值:%s", record.key(), record.value());
        }
    }

    /**
     * 之间我们直接基于主题名字来订阅而没有指定分区的时候, KAFKA 会使用分区器
     * 来帮我们选择对应的分区
     */
    public static void consumerByTopicAndPartition() {
        consumer.subscribe(Collections.singletonList("hello"));
        consumer.assign(Collections.singletonList(new TopicPartition("hello", 0)));

        ConsumerRecords<String, String> poll = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : poll) {
            log.info("键:%s, 值:%s", record.key(), record.value());
        }
    }

    public static void main(String[] args) {

    }
}
