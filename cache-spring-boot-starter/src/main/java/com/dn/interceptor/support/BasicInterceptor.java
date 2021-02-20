package com.dn.interceptor.support;

import com.diga.generic.utils.JsonUtils;
import com.dn.entity.CacheDefinition;
import com.dn.entity.CacheWrapper;
import com.dn.interceptor.ICacheExecutor;
import com.dn.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Set;

@Component("com.dn.interceptor.support.basicInterceptor")
public class BasicInterceptor implements ICacheExecutor {

    @Autowired
    private ICacheService cacheService;

    @Override
    public void doUpdate(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper) {
        // 基本类型的缓存不需要更新信息
    }

    @Override
    public void doInsert(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper) {
        // 把 key 和 val 缓存加入到 redis 中
        String key = cacheWrapper.getKey();
        String val = JsonUtils.stringify(cacheWrapper);
        String group = cacheDefinition.getGroupName();
        long time = cacheDefinition.getTime();

        if (time > 0) {
            cacheService.addCache(key, val, time);
        } else {
            cacheService.addCache(key, val);
        }

        // 把缓存名称也加入对应的缓存组下, 方便清除缓存
        Set<String> groupSet = JsonUtils.toSet(cacheService.getCache(group), String.class);
        if (!groupSet.contains(key)) {
            groupSet.add(key);
            cacheService.addCache(group, JsonUtils.stringify(groupSet));
        }
    }


    @Override
    public void doDelete(CacheDefinition cacheDefinition) {
        // 拿到缓存组名称
        String groupName = cacheDefinition.getGroupName();
        // 把缓存名称也加入对应的缓存组下, 方便清除缓存
        Set<String> groupSet = JsonUtils.toSet(cacheService.getCache(groupName), String.class);

        // 排队枪毙
        for (String cacheName : groupSet) {
            cacheService.setCacheTimeout(cacheName, 0);
        }
    }

    /**
     * 从缓存中获取对应的数据并且完成反序列化
     *
     * @param cacheKey 缓存名称
     * @return
     */
    @Override
    public CacheWrapper doSelect(String cacheKey) {
        String json = cacheService.getCache(cacheKey);
        if (!StringUtils.isEmpty(json)) {
            return JsonUtils.parse(json, CacheWrapper.class);
        }
        return null;
    }
}
