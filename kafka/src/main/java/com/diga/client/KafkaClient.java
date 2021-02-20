package com.diga.client;

import com.google.common.collect.Lists;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class KafkaClient {

    private AdminClient client;

    /**
     * 创建主题
     *
     * @param topicName 主题名称
     * @param partition 分区数, 默认是1
     * @param replicas  备份分片数, 默认是0
     * @return
     */
    public boolean createTopic(String topicName, Integer partition, Short replicas) {
        try {
            NewTopic newTopic = new NewTopic(topicName, partition == null ? 1 : partition, replicas == null ? 0 : replicas);
            client.createTopics(Collections.singleton(newTopic));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 删除主题
     *
     * @param topicName 主题名称
     */
    public boolean deleteTopic(String topicName) {
        try {
            client.deleteTopics(Collections.singletonList(topicName));
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 显示所有主题
     *
     * @return
     */
    public List<String> listTopic() {
        try {
            ListTopicsResult listTopicsResult = client.listTopics();
            return new ArrayList<>(listTopicsResult.names().get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }
}
