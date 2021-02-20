package com.dn.interceptor;

import com.dn.entity.CacheDefinition;
import com.dn.entity.CacheWrapper;


public interface ICacheExecutor {

    /**
     * 更新缓存信息
     *
     * @param cacheDefinition 缓冲描述
     * @param cacheWrapper    缓存包装
     */
    void doUpdate(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper);

    /**
     * 将缓存数据添加到缓存服务中
     *
     * @param cacheDefinition 缓冲描述
     * @param cacheWrapper    缓存包装
     */
    void doInsert(CacheDefinition cacheDefinition, CacheWrapper cacheWrapper);


    /**
     * 删除缓存组中的所有缓存
     *
     * @param cacheDefinition 缓存描述
     */
    void doDelete(CacheDefinition cacheDefinition);

    /**
     * 从缓存中获取对应的数据并且完成反序列化
     *
     * @param cacheKey 缓存名称
     * @return
     */
    CacheWrapper doSelect(String cacheKey);
}
