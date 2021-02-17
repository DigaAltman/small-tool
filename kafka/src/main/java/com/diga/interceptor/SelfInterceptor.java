package com.diga.interceptor;

import com.diga.generic.utils.EncryptionUtil;
import com.diga.generic.utils.log;
import org.apache.kafka.clients.producer.*;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义拦截器, 这里我们同一给发送过去的内容进行一个 BASE64 加密
 * 我们可以通过配置来调用我们的拦截器:
 *      interceptor.classes=com.diga.interceptor.SelfInterceptor
 */
public class SelfInterceptor implements ProducerInterceptor<String, String> {

    AtomicInteger sendSuccess = new AtomicInteger(0);
    AtomicInteger sendFailure = new AtomicInteger(0);

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String value = EncryptionUtil.encryption(record.value());
        return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(), record.key(), value, record.headers());
    }

    /**
     * ACK 确认
     *
     * @param metadata
     * @param exception
     */
    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            // 发送失败...
            sendFailure.incrementAndGet();
        } else {
            sendSuccess.incrementAndGet();
        }
    }

    /**
     * This is called when interceptor is closed
     */
    @Override
    public void close() {
        int count = sendSuccess.addAndGet(sendFailure.intValue());
        log.info("发送总共条数 %d, 成功数: %d, 失败数: %d", count, sendSuccess.intValue(), sendFailure.intValue());
    }

    /**
     * Configure this class with the given key-value pairs
     *
     * @param configs
     */
    @Override
    public void configure(Map<String, ?> configs) {

    }
}
