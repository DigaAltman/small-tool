package com.dn.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 缓存结果包装器
 */
@Data
@NoArgsConstructor
public class CacheWrapper implements Serializable {
    /**
     * 缓存创建时间
     */
    private long createdTime;

    /**
     * 缓存最后一次更新时间
     */
    private long updatedTime;


    /**
     * 缓存的版本号
     */
    private int version;

    /**
     * 真正的缓存数据
     */
    private String json;

    /**
     * 缓存名称
     */
    private String key;

    public CacheWrapper(String key, String value) {
        this.key = key;
        this.json = value;
        this.createdTime = System.currentTimeMillis();
        this.updatedTime = System.currentTimeMillis();
        this.version = 1;
    }

}
