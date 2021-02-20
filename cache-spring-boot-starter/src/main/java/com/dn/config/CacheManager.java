package com.dn.config;

import com.dn.entity.CacheDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存管理部门, 管理 CacheDefinition 和 CacheGroup 之间的关系.
 */
public class CacheManager {
    /**
     * 缓存名称 和 缓存抽象 之间的对应关系
     */
    private Map<String, CacheDefinition> map = new HashMap<>(64);

    public void addCacheDefinition(CacheDefinition cacheDefinition) {
        map.put(cacheDefinition.getName(), cacheDefinition);
    }

    public CacheDefinition getCacheDefinition(String name) {
        return map.get(name);
    }
}
