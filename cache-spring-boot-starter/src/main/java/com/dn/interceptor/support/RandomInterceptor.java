package com.dn.interceptor.support;

import com.dn.common.CacheType;
import com.dn.config.CacheBean;
import com.dn.entity.CacheDefinition;
import com.dn.entity.CacheWrapper;
import com.dn.interceptor.ICacheExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

@Component("com.dn.interceptor.support.randomInterceptor")
public class RandomInterceptor implements ICacheExecutor {
    private static final Random random = new Random();

    @Autowired
    private CacheBean cacheBean;

    @Resource(name = "com.dn.interceptor.support.basicInterceptor")
    private ICacheExecutor cacheInterceptor;

    /**
     * 更新缓存信息
     *
     * @param cacheDefinition 缓冲描述
     * @param cacheWrapper    缓存包装
     */
    @Override
    public void doUpdate(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper) {
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
        // 获取原来的过期时间
        long oldTime = cacheDefinition.getTime();

        // 如果这个缓存类型是随机缓存
        if (cacheDefinition.getType() == CacheType.RANDOM) {
            int randomTime = random.nextInt(Math.max(cacheBean.getRandomTime(), 5));
            cacheDefinition.setTime(randomTime + oldTime);
        }

        cacheInterceptor.doInsert(cacheDefinition, cacheWrapper);

        // 修改回原来的过期时间. 这样缓存中的时间就会刷新回来
        cacheDefinition.setTime(oldTime);
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
