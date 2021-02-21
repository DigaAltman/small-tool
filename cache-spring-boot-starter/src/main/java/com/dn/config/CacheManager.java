package com.dn.config;

import com.diga.generic.utils.JsonUtils;
import com.dn.entity.CacheDefinition;
import com.dn.service.ICacheService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 缓存管理部门, 管理 CacheDefinition 和 CacheGroup 之间的关系.
 */
@Slf4j
public class CacheManager {
    @Setter
    private ICacheService cacheService;

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

    /**
     * 初始化缓存组
     */
    public void initCacheGroup() {
        map.forEach((k, v) -> {
            String groupName = v.getGroupName();
            String cache = cacheService.getCache(groupName);
            if (StringUtils.isEmpty(cache)) {
                log.info("创建缓存组 {}", groupName);
                cacheService.addCache(groupName, JsonUtils.stringify(new HashSet()));
            }
        });
    }
}
