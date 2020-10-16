package com.diga.zookeeper;

import com.diga.generic.utils.PropUtils;
import lombok.SneakyThrows;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestZookeeper {

    private ZooKeeper zookeeper = null;

    @Before
    public void connection() throws InterruptedException {
        PropUtils.KV kv = PropUtils.load("classpath:zk.properties");
        String url = kv.getString("zk.ip") + ":" + kv.getString("zk.port");

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            zookeeper = new ZooKeeper(url, 5000, watchedEvent -> {
                if (watchedEvent.getState() == Watcher.Event.KeeperState.SaslAuthenticated) {
                    System.out.println("连接创建成功");
                    countDownLatch.countDown();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        System.out.println("连接 id 为:" + zookeeper.getSessionId());
    }

    /**
     * 创建节点
     *
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void create() throws KeeperException, InterruptedException {
        zookeeper.create("/a/node1", "Zookeeper".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 创建一个只读权限的节点
     */
    @Test
    public void createOnlyRead() throws KeeperException, InterruptedException {
        zookeeper.create("/a/node2", "Only Read Auth".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    /**
     * 使用 world 模式,创建一个自定义权限列表
     */
    @Test
    @SneakyThrows
    public void createSelfAuth() {
        List<ACL> aclList = new ArrayList();
        Id id = new Id("world", "anyone");
        aclList.add(new ACL(ZooDefs.Perms.CREATE, id));
        aclList.add(new ACL(ZooDefs.Perms.DELETE, id));

        zookeeper.create("/a/node3", "Self Auth Zookeeper Node".getBytes(), aclList, CreateMode.PERSISTENT);
    }

    /**
     * 使用 ip 模式进行授权
     */
    @Test
    @SneakyThrows
    public void createIpAuth() {
        List<ACL> aclList = new ArrayList();
        Id id = new Id("ip", "47.112.125.251");
        aclList.add(new ACL(ZooDefs.Perms.CREATE, id));
        zookeeper.create("/a/node4", "Zookeeper Value By Ip Auth".getBytes(), aclList, CreateMode.PERSISTENT);
    }


    @Test
    @SneakyThrows
    public void createDigestAuth() {
        List<ACL> aclList = new ArrayList();
        Id id = new Id("digest", "123456");
        aclList.add(new ACL(ZooDefs.Perms.CREATE, id));
        zookeeper.create("/a/node5", "123456".getBytes(), aclList, CreateMode.PERSISTENT);
    }

    /**
     * 异步创建节点
     */
    @Test
    @SneakyThrows
    public void asyncCreateNode() {
        zookeeper.create("/a/node6", "async node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, (rc, path, ctx, name) -> {
            System.out.println(name);
        }, "context");
    }

    @After
    public void close() throws Exception {
        zookeeper.close();
    }

}
