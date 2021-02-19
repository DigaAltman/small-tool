<img src="../生产者消息投递流程.png"></img>
消息发送的过程中, 会涉及 Thread-main 和 Thread-send 两个线程协调工作, 主线程首先会将业务数据封装成 ProducerRecord 对象, 
之后调用 send() 方法将消息放入到 RecordAccumulator 中暂存, Thread-Send 负责将消费信息构成请求, 并执行网络I/O的线程, 将
RecordAccumulator 中的消息批量发送出去.




## 位移提交
对于 KAFKA 中的分区而言, 它的每条消息都有唯一的 offset, 用来表示消息在分区中的位置.

当我们调用 poll() 时, 该方法会返回我们没有消费的消息, 当消息从 broker 返回消费者时, broker 并不追踪这些消息
是否被消费者接收到; KAFKA 让消费者自身来管理消费的位移, 并向消费者提供更新位移的接口, 这种更新位移方式称为提交(commit)


## KAFKA 重复消费
重复消费归根到底的原因: 已经消费了数据，但是 offset 没提交 (KAFKA 没有或者不知道该数据已经被消费)
下面几种原因会造成 KAFKA 的重复消费:

1. 强行kill线程，导致消费后的数据，offset 没有提交（消费系统宕机、重启等）

2. 设置offset为自动提交，关闭kafka时，如果在close之前，调用 consumer.unsubscribe() 则有可能部分offset没提交，下次重启会重复消费

3. 消费后的数据，当offset还没有提交时，partition就断开连接。比如，通常会遇到消费的数据，处理很耗时，导致超过了Kafka的session timeout时间（0.10.x版本默认是30秒），那么就会re-blance重平衡，此时有一定几率offset没提交，会导致重平衡后重复消费。 

4. 当消费者重新分配partition的时候，可能出现从头开始消费的情况，导致重发问题

5. 当消费者消费的速度很慢的时候，可能在一个session周期内还未完成，导致心跳机制检测报告出问题

这里我们可以用一个 流水记录表 来解决重复消费的问题. 比如: 给每条消息一个唯一ID, 当我们消费此数据时, 可以将这个 id 存入到 mysql 或者 redis 中, 通过查询
是否存在这条ID记录来判断任务是否被重复消费.


## KAFKA 消息丢失
一般 KAFKA 的副本挂了, 但是数据还没同步到其他的 follower 副本, 然后重新选举 Leader 后,就造成 Kafka 数据丢失了.

KAFKA 消息丢失解决方案:
1. 设置四个参数:
```properties
# 为每一个 topic 设置此选项, 要求每个分区至少存在两个副本
replication.factor={{value >= 2}}

# 要求每个 leader 最少感知到有一个 follower 还与自己保持联系,没有掉队.
min.insync.replicas={{value >= 2}}

# 要求每条数据写入 replica 后,才算写入成功.
acks=all

# 写入失败,无限重试
retries=Integer.MAX_VALUE
```

