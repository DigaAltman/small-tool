## KAFKA 生产者发送消息的幂等性
Producer 的幂等性指的是当发送同一条消息时，数据在 Server 端只会被持久化一次，数据不丟不重，但是这里的幂等性是有条件的
> 1. 只能保证 Producer 在单个会话内不丟不重，如果 Producer 出现意外挂掉再重启是无法保证的（幂等性情况下，是无法获取之前的状态信息，因此是无法做到跨会话级别的不丢不重）
>
> 2. 幂等性不能跨多个 Topic-Partition，只能保证单个 partition 内的幂等性，当涉及多个 Topic-Partition 时，这中间的状态并没有同步
>


## KAFKA 事务
为了解决上面的幂等性不能解决的问题. 比如跨多个 Topic-Partition 保证数据在 Server 端只会被持久化一次
使用方式: 
```properties
# 开启 KAFKA 事务, 需要注意的是 acks 必须为 all
enable.idempotence=true
```

幂等性是通过两个关键信息保证的，PID 和 sequence numbers. 

| 关键字               | 意义                                                         |
| -------------------- | ------------------------------------------------------------ |
| **PID**              | 用来标识每个producer client                                  |
| **sequence numbers** | 客户端发送的每条消息都会带相应的 sequence number，Server 端就是根据这个值来判断数据是否重复 |



producer 初始化会由 server 端生成一个 PID, 然后发送每条信息都包含该 PID 和 sequence number, 在 server 端，是按照 partition 同样存放一个 sequence numbers 信息，通过判断客户端发送过来的 sequence number 与 server 端 number+1 差值来决定数据是否重复或者漏掉.

通常情况下为了保证数据顺序性, 我们可以通过`max.in.flight.requests.per.connection=1`来保证, 这个也只是针对单实例. 在 kafka2.0+ 版本上, 只要开启幂等性, 不用设置这个参数也能保证发送数据的顺序性.
