package com.dn;

import com.diga.generic.utils.ClassUtils;
import com.diga.generic.utils.JsonUtils;
import com.diga.generic.utils.MethodUtils;
import com.dn.annotation.Cache;
import com.dn.config.CacheBean;
import com.dn.service.ICacheService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ICacheService cacheService;

    private CacheBean cacheBean;

    public CacheApplicationEvent(ICacheService cacheService, CacheBean cacheBean) {
        this.cacheService = cacheService;
        this.cacheBean = cacheBean;
    }

    /**
     * 基于 Spring 的 ApplicationListener 进行的扩展, 当整个 Spring 的 Bean全部解析完成后, 我们
     * 会针对 Spring 的 Bean 进行扫描. 解析包含 @Cache 注解的 SpringBean, 然后将他们的返回值类型和
     * 返回值类型中的泛型放到 MethodUtils 中对应的缓存中. 当下次获取的时候, 我们就可以直接从缓存中获取,
     * 从而提升反射的性能
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        GenericApplicationContext app = (GenericApplicationContext) event.getApplicationContext();
        String[] beanDefinitionNames = app.getBeanDefinitionNames();


        // 这里我们先搞一波本地 group 添加记录吧. 减少使用缓存服务中的 getCache. 这里使用局部变量记录一下就好了
        Set<String> groupSet = new HashSet<>();


        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = app.getBeanDefinition(beanDefinitionName);

            // 存在一些 FactoryBean 没有 beanClassName 属性, 这里我们需要过滤掉
            if (!StringUtils.isEmpty(beanDefinition.getBeanClassName())) {

                // 获取 SpringBean 对应的原始 Class 类型
                Class beanClass = ClassUtils.tryForName(beanDefinition.getBeanClassName());
                if (beanClass == null) {
                    continue;
                }

                Method[] methods = beanClass.getMethods();

                for (Method method : methods) {
                    // 从方法上寻找 @Cache 注解, 推荐使用 AnnotationUtils
                    Cache cache = AnnotationUtils.findAnnotation(method, Cache.class);

                    if (cache != null) {
                        // 初始化 缓存组
                        String groupName = cacheBean.getGroupNamePrefix() + cache.group();

                        // 如果本地记录没有包含
                        if (!groupSet.contains(groupName)) {
                            // 看看第三方缓存是否包含
                            if (StringUtils.isEmpty(cacheService.getCache(groupName))) {
                                cacheService.addCache(groupName, JsonUtils.stringify(Lists.newArrayList()));
                            }
                            groupSet.add(groupName);
                        }

                        String methodId = MethodUtils.getMethodId(method);

                        MethodUtils.getMethodGenericTypes(method);
                        MethodUtils.getMethodReturnType(method);

                        log.info("扫描到方法 {} 存在 @Cache 注解", methodId);
                    }
                }
            }
        }
    }
}
