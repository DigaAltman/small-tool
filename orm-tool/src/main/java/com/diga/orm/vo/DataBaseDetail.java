package com.diga.orm.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DataBaseDetail implements Serializable {
    /**
     * 数据库的字符集
     */
    private String charset;

    /**
     * 数据库的存储引擎
     */
    private String engine;

    /**
     * 数据库的版本号
     */
    private String version;

    /**
     * 数据库的大小
     */
    private BigDecimal dataSize;

    /**
     * 索引大小
     */
    private BigDecimal indexSize;

    /**
     * 缓冲区大小
     */
    private BigDecimal cacheSize;

    /**
     * 当前用户
     */
    private String username;

    /**
     * 数据库最大连接数量
     */
    private Integer maxConnectionNumber;

    /**
     * 缓存线程数
     */
    private Integer cacheThreadNumber;

    /**
     * 连接线程数
     */
    private Integer connectThreadSize;

    /**
     * 创建线程数
     */
    private Integer createdThreadSize;

    /**
     * 运行线程数
     */
    private Integer runThreadSize;

    /**
     * 数据存放服务器目录
     */
    private String dataDir;
}
