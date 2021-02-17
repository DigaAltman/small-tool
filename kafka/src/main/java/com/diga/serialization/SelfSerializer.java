package com.diga.serialization;

import com.diga.generic.utils.JsonUtils;
import com.diga.generic.utils.log;
import com.diga.pojo.DataWrapper;
import org.apache.commons.collections.MapUtils;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 基于自定义的 DataWrapper 类的序列化器. 如果我们需要使用这个序列化器,
 * 我们直接在 properties 对象中设置:
 * <code>
 *  key.serializer=org.apache.kafka.common.serialization.SelfSerializer
 *  value.serializer=org.apache.kafka.common.serialization.SelfSerializer
 * </code>
 */
public class SelfSerializer implements Serializer<DataWrapper> {

    private String encoding = "UTF8";

    /**
     * KAFKA 会将配置信息通过这个方法传入进来
     *
     * @param map 具体的参数 K-V
     * @param b   通过这个参数来判断此时是对 key 序列化, 还是对 val 序列化
     */
    @Override
    public void configure(Map<String, ?> map, boolean b) {
        // 这里我们可以参考StringSerializer, 判断读取用户是否配置了自定义字符集
        String propertyName = b ? "key.serializer.encoding" : "value.serializer.encoding";
        this.encoding = MapUtils.getString(map, propertyName, encoding);
    }

    /**
     * 序列化操作
     *
     * @param s           topic名称
     * @param dataWrapper 具体序列化的对象
     * @return
     */
    @Override
    public byte[] serialize(String s, DataWrapper dataWrapper) {
        byte[] bytes = null;
        try {
            bytes = JsonUtils.stringify(dataWrapper).getBytes(this.encoding);
        } catch (UnsupportedEncodingException e) {
            log.error("序列化对象失败, 具体错误原因: %s", e.getMessage());
        }
        return bytes;
    }

    @Override
    public void close() {
        // 啥也不做...
    }

}
