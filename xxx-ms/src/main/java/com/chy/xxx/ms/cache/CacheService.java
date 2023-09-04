package com.chy.xxx.ms.cache;

import javax.annotation.Nullable;

/**
 * 缓存服务。可通过此接口切换缓存实现，注入CacheService时指定beanName
 *
 * @author chy
 */
public interface CacheService {

    /**
     * 查询缓存
     *
     * @param cacheKey 缓存key
     * @return Object
     */
    @Nullable
    Object getValue(String cacheKey);

    /**
     * 设置缓存<br/>
     * 注：此方法仅用于初始化缓存数据，其它情况统一以 removeCache() 删除方式来更新缓存
     *
     * @param cacheKey   缓存key
     * @param value      值
     * @param expiration 有效期，单位：秒
     */
    void setCache(String cacheKey, Object value, long expiration);

    /**
     * 清除缓存
     *
     * @param cacheKey 缓存key
     */
    void removeCache(String cacheKey);

}
