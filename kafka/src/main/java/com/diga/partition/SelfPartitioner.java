package com.diga.partition;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class SelfPartitioner implements Partitioner {

    /**
     * Compute the partition for the given record.
     *
     * @param topic      The topic name
     * @param key        The key to partition on (or null if no key)
     * @param keyBytes   The serialized key to partition on( or null if no key)
     * @param value      The value to partition on or null
     * @param valueBytes The serialized value to partition on or null
     * @param cluster    The current cluster metadata
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 拿到这个 topic 对应的分区总数
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int partitionCount = partitions.size();

        // 现在我们不根据 key 来进行分区了, 我们根据消息的创建时间来进行分区. 假设我们这个 KAFKA 是用来做定时任务的.
        long time = System.currentTimeMillis();

        return (int) time % partitionCount;
    }

    /**
     * This is called when partitioner is closed.
     */
    @Override
    public void close() {

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
