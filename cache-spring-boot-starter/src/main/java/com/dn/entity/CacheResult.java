package com.dn.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * 缓存结果包装器
 */
@Data
public class CacheResult implements Serializable {
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

}
