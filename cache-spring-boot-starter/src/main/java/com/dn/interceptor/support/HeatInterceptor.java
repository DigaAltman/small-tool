package com.dn.interceptor.support;

import com.dn.common.CacheType;
import com.dn.config.CacheBean;
import com.dn.entity.CacheDefinition;
import com.dn.entity.CacheWrapper;
import com.dn.interceptor.ICacheExecutor;
import com.dn.service.ICacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("com.dn.interceptor.support.heatInterceptor")
public class HeatInterceptor implements ICacheExecutor {

    @Resource(name = "com.dn.interceptor.support.randomInterceptor")
    private ICacheExecutor cacheInterceptor;

    @Autowired
    private CacheBean cacheBean;

    @Autowired
    private ICacheService cacheService;

    /**
     * 更新缓存信息
     *
     * @param cacheDefinition 缓冲描述
     * @param cacheWrapper    缓存包装
     */
    @Override
    public void doUpdate(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper) {
        if (cacheDefinition.getType() == CacheType.HEAT) {
            String key = cacheWrapper.getKey();
            int heatTime = Math.max(cacheBean.getHeatTime(), 5);
            // 还剩多少时间
            long cacheTimeout = cacheService.getCacheTimeout(key);
            // 马上续命
            cacheService.setCacheTimeout(key, heatTime + cacheTimeout);
        }
        cacheInterceptor.doUpdate(cacheDefinition, cacheWrapper);
    }

    /**
     * 将缓存数据添加到缓存服务中
     *
     * @param cacheDefinition 缓冲描述
     * @param cacheWrapper    缓存包装
     */
    @Override
    public void doInsert(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper) {
        cacheInterceptor.doInsert(cacheDefinition, cacheWrapper);
    }

    /**
     * 删除缓存组中的所有缓存
     *
     * @param cacheDefinition 缓存描述
     */
    @Override
    public void doDelete(CacheDefinition cacheDefinition) {
        cacheInterceptor.doDelete(cacheDefinition);
    }

    /**
     * 从缓存中获取对应的数据并且完成反序列化
     *
     * @param cacheKey 缓存名称
     * @return
     */
    @Override
    public CacheWrapper doSelect(String cacheKey) {
        return cacheInterceptor.doSelect(cacheKey);
    }
}
