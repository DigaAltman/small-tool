package com.dn.entity;

import com.dn.common.CacheType;
import com.dn.serialize.ICacheSerialize;
import lombok.Data;

import java.io.Serializable;

@Data
public class CacheEntity implements Serializable {
    // 缓存名称
    private String cacheName;

    // 缓存所属组名称
    private String groupName;

    // 缓存类型
    private CacheType type;

    // 序列化服务
    private ICacheSerialize cacheSerialize;

    /**
     * 生成 CacheEntity 的方法
     *
     * @param cacheName
     * @param groupName
     * @param type
     * @return
     */
    public static CacheEntity generate(String cacheName, String groupName, CacheType type, ICacheSerialize cacheSerialize) {
        CacheEntity entity = new CacheEntity();
        entity.setCacheName(cacheName);
        entity.setGroupName(groupName);
        entity.setType(type);
        entity.setCacheSerialize(cacheSerialize);
        return entity;
    }

}
