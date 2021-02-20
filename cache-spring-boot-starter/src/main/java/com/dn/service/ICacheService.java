package com.dn.service;

/**
 * 此接口需要用户自定义实现类
 */
public interface ICacheService {

    /**
     * 添加缓存, 并且设置缓存的有效期
     *
     * @param cacheName
     * @param cacheValue
     * @param cacheTime
     */
    void addCache(String cacheName, String cacheValue, long cacheTime);

    /**
     * 添加缓存, 但不设置缓存的有效期
     *
     * @param cacheName
     * @param cacheValue
     */
    void addCache(String cacheName, String cacheValue);

    /**
     * 获取缓存名称对应的缓存值
     *
     * @param cacheName
     * @return
     */
    String getCache(String cacheName);

    /**
     * 获取缓存的超时时间
     *
     * @param cacheName
     * @return
     */
    long getCacheTimeout(String cacheName);

    /**
     * 设置缓存的超时时间
     *
     * @param cacheName
     * @param cacheTime
     * @return
     */
    long setCacheTimeout(String cacheName, long cacheTime);


    /**
     * 此缓存是否是不过期缓存
     *
     * @param cacheName
     * @return
     */
    boolean isInfiniteTime(String cacheName);
}
