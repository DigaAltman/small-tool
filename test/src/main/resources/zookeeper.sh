#!/bin/bash

# 1. 下载 zookeeper 安装包
wget http://mirrors.hust.edu.cn/apache/zookeeper/zookeeper-3.4.6/zookeeper-3.4.6.tar.gz

# 2. 将 zookeeper 存放在 /opt 目录下
mkdir /opt/zookeeper

# 3. 解压到 /opt/zookeeper 目录
tar -zxvf zookeeper-3.4.6.tar.gz -C /opt/zookeeper/
