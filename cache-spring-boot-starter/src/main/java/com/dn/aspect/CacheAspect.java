package com.dn.aspect;

import com.diga.generic.utils.MethodUtils;
import com.diga.generic.utils.ReflexUtils;
import com.dn.annotation.ClearCache;
import com.dn.config.CacheManager;
import com.dn.entity.CacheDefinition;
import com.dn.entity.CacheWrapper;
import com.dn.interceptor.ICacheExecutor;
import com.dn.serialize.ICacheSerialize;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class CacheAspect {

    @Autowired
    private ICacheExecutor cacheExecutor;

    // 针对 @Cache 的切面
    @Around("@annotation(com.dn.annotation.Cache)")
    public Object handleCache(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();

        CacheManager cacheManager = ReflexUtils.getSingletonInstance(CacheManager.class);

        // 获取常态方法的唯一标识, 也就是不是运行时候的方法的唯一标识
        String methodName = MethodUtils.getMethodId(method);

        // 从缓存中获取对应的 CacheDefinition
        CacheDefinition cacheDefinition = cacheManager.getCacheDefinition(methodName);

        // 获取这个方法对应的缓存名称
        String cacheName = String.format(cacheDefinition.getName(), args);

        if (log.isDebugEnabled() || log.isInfoEnabled()) {
            log.info("类 [{}] 下的方法 [{}] 对应的缓存名称为: {}", jp.getTarget().getClass().getName(), method.getName(), cacheName);
        }

        // 尝试从 redis 中获取缓存
        CacheWrapper cacheWrapper = cacheExecutor.doSelect(cacheName);

        if (cacheWrapper != null) {
            ICacheSerialize cacheSerialize = cacheDefinition.getCacheSerialize();

            // 更新数据
            cacheExecutor.doUpdate(cacheDefinition, cacheWrapper);

            // 将缓存中的结果返回出去
            String result = cacheWrapper.getJson();
            return cacheSerialize.deserialization(result, cacheDefinition.getReturnType(), cacheDefinition.getGenericTypes());
        }

        // 没有拿到缓存.
        Object proceed = jp.proceed(args);

        // 如果方法没有返回值, 那就没必要序列化了
        if (!void.class.isAssignableFrom(cacheDefinition.getReturnType())) {
            if (log.isDebugEnabled() || log.isInfoEnabled()) {
                log.info("类 [{}] 下的方法 [{}] 需要被缓存", jp.getTarget().getClass().getName(), method.getName());
            }

            String serialize = cacheDefinition.getCacheSerialize().serialize(proceed);
            cacheWrapper = new CacheWrapper(cacheName, serialize);

            // 插入数据
            cacheExecutor.doInsert(cacheDefinition, cacheWrapper);
        }

        return proceed;
    }


    // 针对 @ClearCache 的切面
    @Around("@annotation(com.dn.annotation.ClearCache)")
    public Object handleClearCache(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Object[] args = jp.getArgs();

        Object proceed = jp.proceed(args);
        ClearCache clearCache = method.getAnnotation(ClearCache.class);

        // 这里是在方法调用完毕后清除缓存
        String groupName = clearCache.group() == null ? method.getDeclaringClass().getName() : clearCache.group();
        CacheDefinition cacheDefinition = new CacheDefinition();
        cacheDefinition.setGroupName(groupName);
        cacheExecutor.doDelete(cacheDefinition);

        return proceed;
    }
}
