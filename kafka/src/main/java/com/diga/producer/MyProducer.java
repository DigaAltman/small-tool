package com.diga.producer;

import com.diga.generic.utils.PropUtils;
import com.diga.generic.utils.URLUtils;
import com.diga.generic.utils.log;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MyProducer {
    // 加载配置
    private static final Properties properties = PropUtils.get(URLUtils.classpath("kafka-producer.properties"));
    // 创建生产者
    private static final KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
    // 定义我们操作的 TOPIC 的名称
    private static final String topicName = "hello";


    /**
     * 同步发送, 发送完成后返回一个 Future<RecordMetadata> 对象. 我们可以通过 Future 对象的 get 方法等待 KAFKA 响应
     * 如果 KAFKA 正常响应, 返回有个 RecordMetadata 对象, 这个对象里面存在一个 offset 属性
     *
     * 如果发送失败, 则会抛出异常, 我们便可以进行异常处理
     *
     */
    public static void simpleProduce() {
        // 批量发送不带回调的信息
        for (int i = 0; i < 10; i++) {
            Future<RecordMetadata> sendFuture = producer.send(new ProducerRecord<>(topicName, "hello kafka" + i));
            try {
                RecordMetadata recordMetadata = sendFuture.get();
                int partition = recordMetadata.partition();
                long offset = recordMetadata.offset();
                log.info("数据发送到分区: %d, 它的偏移量是: %d", partition, offset);
            } catch (InterruptedException | ExecutionException e) {
                // 具体的异常处理逻辑, 比如: 重试, 持久化错误消息...
            }

        }
    }

    /**
     * 异步发送, 发送成功后回调 Callback 方法, 里面存放了 RecordMetadata 对象和 Exception 信息. 如果 Exception 对象为 null 则表示
     * 发送成功
     */
    public static void callbackProduce() {
        Callback callback = (recordMetadata, e) -> {
            if (e == null) {
                int partition = recordMetadata.partition();
                long offset = recordMetadata.offset();
                log.info("数据发送到分区: %d, 它的偏移量是: %d", partition, offset);
            }
        };

        // 批量发送不带回调的信息
        for (int i = 0; i < 10; i++) {
            producer.send(new ProducerRecord<>(topicName, "hello kafka" + i), callback);
        }
    }


    /**
     * 带 KEY 的消息发送
     */
    public static void partitionProduce(String key, String value) {
        producer.send(new ProducerRecord<String, String>("hello", key, value));
    }


    public static void close() {
        // 关闭生产者
        producer.close();
    }

    public static void main(String[] args) {
        try {
            // simpleProduce();
            // callbackProduce();
            partitionProduce("RockStar", "皮尔逊");
        } finally {
            close();
        }
    }
}
