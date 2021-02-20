package com.dn;

import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.JsonUtils;
import com.diga.generic.utils.MethodUtils;
import com.diga.generic.utils.ReflexUtils;
import com.dn.annotation.Cache;
import com.dn.annotation.ClearCache;
import com.dn.config.CacheBean;
import com.dn.config.CacheManager;
import com.dn.entity.CacheDefinition;
import com.dn.entity.reader.CacheDefinitionReader;
import com.dn.service.ICacheService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CacheApplicationEvent implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * 基于 Spring 的 ApplicationListener 进行的扩展, 当整个 Spring 的 Bean全部解析完成后, 我们
     * 会针对 Spring 的 Bean 进行扫描. 解析包含 @Cache 注解的 SpringBean, 然后将他们的返回值类型和
     * 返回值类型中的泛型放到 MethodUtils 中对应的缓存中. 当下次获取的时候, 我们就可以直接从缓存中获取,
     * 从而提升反射的性能.
     *
     * 第一章: 缓存系统起源
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        CacheManager cacheManager = ReflexUtils.getSingletonInstance(CacheManager.class);

        GenericApplicationContext app = (GenericApplicationContext) event.getApplicationContext();
        // 拿到所有 Bean 名称
        String[] beanDefinitionNames = app.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            // 获取 Bean 的抽象描述类
            BeanDefinition beanDefinition = app.getBeanDefinition(beanDefinitionName);

            // 存在一些 FactoryBean 没有 beanClassName 属性, 这里我们需要过滤掉
            if (!StringUtils.isEmpty(beanDefinition.getBeanClassName())) {
                // 获取 SpringBean 对应的原始 Class 类型
                Class beanClass = ClassUtils.tryForName(beanDefinition.getBeanClassName());
                if (beanClass == null) {
                    continue;
                }

                // 拿到这个类下的所有方法
                Method[] methods = beanClass.getMethods();

                for (Method method : methods) {
                    // 从方法上寻找 @Cache 注解, 这里直接使用 Spring 的 AnnotationUtils
                    Cache cache = AnnotationUtils.findAnnotation(method, Cache.class);

                    // 找到 @Cache
                    if (cache != null) {
                        // 解析信息
                        CacheDefinition cacheDefinition = CacheDefinitionReader.readCacheDefinition(cache, method);

                        // 添加进去
                        cacheManager.addCacheDefinition(cacheDefinition);
                        log.info("扫描到方法名称为 {} 上存在 @Cache 注解", cacheDefinition.getName());
                    }
                }
            }
        }
    }
}
