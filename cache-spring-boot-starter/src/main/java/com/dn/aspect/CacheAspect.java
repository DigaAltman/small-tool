package com.dn.aspect;

import com.diga.generic.utils.MethodUtils;
import com.diga.generic.utils.ReflexUtils;
import com.dn.annotation.Cache;
import com.dn.config.CacheBean;
import com.dn.entity.CacheEntity;
import com.dn.serialize.ICacheSerialize;
import com.dn.service.ICacheService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Aspect
public class CacheAspect {
    private ICacheService cacheService;
    private CacheBean cacheBean;
    private static final Map<String, CacheEntity> cacheEntityMap = new HashMap<>(64);


    public CacheAspect(ICacheService cacheService, CacheBean cacheBean) {
        this.cacheService = cacheService;
        this.cacheBean = cacheBean;
    }

    @Around("@annotation(com.dn.annotation.Cache)")
    public Object handleCache(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();

        // 获取常态方法的唯一标识, 也就是不是运行时候的方法的唯一标识
        String keyName = MethodUtils.getMethodId(method);

        // 尝试从缓存中获取对应的 CacheEntity
        CacheEntity cacheEntity = cacheEntityMap.get(keyName);

        if (cacheEntity == null) {
            // 获取 cache 注解
            Cache cache = method.getAnnotation(Cache.class);

            // 如果用户没有指定缓存名称, 那就取方法的唯一标识, 也就是 方法名称+参数类型+具体参数值 作为唯一标识
            String cacheName = StringUtils.isEmpty(cache.name()) ? MethodUtils.getMethodId(method, args) : cache.name();
            String groupName = cacheBean.getGroupNamePrefix() + cache.group();

            // 使用单例进行序列化服务的初始化, 减少类的创建带来的性能损耗
            Class<? extends ICacheSerialize> serialize = cache.serialize();
            ICacheSerialize singletonInstance = ReflexUtils.getSingletonInstance(serialize);

            cacheEntity = CacheEntity.generate(cacheName, groupName, cache.type(), singletonInstance);
            cacheEntityMap.put(keyName, cacheEntity);
        }

        if (log.isDebugEnabled() || log.isInfoEnabled()) {
            log.info("类 [{}] 下的方法 [{}] 对应的缓存名称为: {}", jp.getTarget().getClass().getName(), method.getName(), cacheEntity.getCacheName());
        }

        String json = cacheService.getCache(cacheEntity.getCacheName());

        if (StringUtils.isEmpty(json)) {

        }

        // 尝试获取已经被缓存的数据
        ICacheSerialize cacheSerialize = cacheEntity.getCacheSerialize();


        return null;
    }


}
