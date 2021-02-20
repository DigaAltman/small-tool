package com.diga.consumer;

import com.diga.generic.utils.log;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class CommitConsumer {
    /**
     * 自动提交
     */
    public static void autoCommit() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "122.51.126.135:9092");
        properties.put("group.id", "kafka-client-consumer");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "2000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("hello"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                log.info("offset=%d, key=%s, value=%s%n", record.offset(), record.key(), record.value());
            }
        }
    }

    /**
     * 手动同步提交
     */
    public static void manualCommitSync() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "122.51.126.135:9092");
        properties.put("group.id", "kafka-client-consumer");
        properties.put("enable.auto.commit", "false");
        properties.put("auto.commit.interval.ms", "2000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("hello"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            // 处理消息
            process(records);
            try {
                // 手动同步提交偏移量
                consumer.commitSync();
            } catch (CommitFailedException e) {
                // 处理提交失败的异常
                handle(e);
            }
        }
    }


    /**
     * 手动异步提交
     *
     * commitAsync 不能够替代 commitSync. commitAsync 的问题在于，出现问题时它不会自动重试. 因为它是异步操作，倘若提交失败后自动重试，
     * 那么它重试时提交的位移值可能早已经 '过期' 或不是最新值了. 因此，异步提交的重试其实没有意义，所以 commitAsync 是不会重试的.
     *
     * 手动提交, 我们需要将 commitSync 和 commitAsync 组合使用才能到达最理想的效果, 原因有两个:
     *      1. 我们可以利用 commitSync 的自动重试来规避那些瞬时错误, 比如网络的瞬时抖动, Broker 端 GC 等. 因为这些问题都是短暂的, 自动重试通常都会成功,
     *      因此, 我们不想自己重试, 而是希望 Kafka Consumer 帮我们做这件事. 我们不希望程序总处于阻塞状态, 影响 TPS.
     *
     *      2. 我们不希望程序总处于阻塞状态, 影响 TPS
     */
    public static void manualCommitAsync() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "122.51.126.135:9092");
        properties.put("group.id", "kafka-client-consumer");
        properties.put("enable.auto.commit", "false");
        properties.put("auto.commit.interval.ms", "2000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList("hello"));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);

                // 处理消息
                process(records);
                // 使用异步提交规避阻塞
                consumer.commitAsync();
            }
        } catch (Exception e) {
            handle(e);
        } finally {
            try {
                consumer.commitSync(); // 最后一次提交使用同步阻塞式提交
            } finally {
                consumer.close();
            }
        }
    }


    // 处理消息的具体业务逻辑
    private static void process(ConsumerRecords<String, String> records) {

    }

    // 处理异常的具体业务逻辑
    private static void handle(Exception e) {

    }


    /**
     * KAFKA 指定位移消费
     */
    public static void specifiedDisplacement() {
        
    }
}
