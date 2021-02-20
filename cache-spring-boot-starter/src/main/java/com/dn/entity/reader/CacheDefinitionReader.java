package com.dn.entity.reader;

import com.diga.generic.utils.MethodUtils;
import com.diga.generic.utils.ReflexUtils;
import com.dn.annotation.Cache;
import com.dn.common.CacheType;
import com.dn.entity.CacheDefinition;
import com.dn.serialize.ICacheSerialize;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class CacheDefinitionReader {

    /**
     * 读取缓存信息
     *
     * @param cache  缓存
     * @param method 方法
     * @return
     */
    public static CacheDefinition readCacheDefinition(Cache cache, Method method) {
        CacheDefinition cacheDefinition = new CacheDefinition();

        // 你的名字?
        String cacheName = cache.name();
        if (StringUtils.isEmpty(cacheName)) {
            cacheName = MethodUtils.getMethodId(method);
        }

        // 你的上级?
        String cacheGroupName = cache.group();
        if (StringUtils.isEmpty(cacheGroupName)) {
            cacheGroupName = method.getDeclaringClass().getName();
        }

        long cacheTime = cache.time();

        // 你的性格?
        CacheType cacheType = cache.type();

        // 接头暗号?
        ICacheSerialize cacheSerialize = ReflexUtils.getSingletonInstance(cache.serialize());

        // 返回值类型中的泛型 和 返回值类型
        List<Class> genericTypes = MethodUtils.getMethodGenericTypes(method);
        Class returnType = MethodUtils.getMethodReturnType(method);

        cacheDefinition.setName(cacheName);
        cacheDefinition.setTime(cacheTime);
        cacheDefinition.setGroupName(cacheGroupName);
        cacheDefinition.setType(cacheType);
        cacheDefinition.setCacheSerialize(cacheSerialize);
        cacheDefinition.setGenericTypes(genericTypes);
        cacheDefinition.setReturnType(returnType);
        return cacheDefinition;
    }

}
