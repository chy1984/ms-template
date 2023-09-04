package com.chy.xxx.ms.cache;

import com.chy.xxx.ms.component.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;

/**
 * redis缓存服务实现<br/>
 * 说明：发生异常时不往外抛，不影响业务处理，可根据返回值判断操作结果
 *
 * @author chy
 */
@Slf4j
@Service("redisCacheService")
public class RedisCacheServiceImpl implements CacheService {

    @Resource
    private RedisService redisService;

    @Nullable
    @Override
    public Object getValue(String cacheKey) {
        return redisService.get(cacheKey);
    }

    @Override
    public void setCache(String cacheKey, Object value, long expiration) {
        redisService.set(cacheKey, value, expiration);
        log.info("已设置缓存，cacheKey={}，value={}", cacheKey, value);
    }

    @Override
    public void removeCache(String cacheKey) {
        redisService.del(cacheKey);
        log.info("已清除缓存，cacheKey={}", cacheKey);
    }

}
