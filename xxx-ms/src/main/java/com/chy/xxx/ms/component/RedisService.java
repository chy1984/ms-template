package com.chy.xxx.ms.component;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis操作封装，提供redis基础、通用功能
 *
 * @author chy
 */
@Component
public class RedisService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*************************************** key操作 ****************************************/

    public void set(String key, Object value, Duration expiration) {
        redisTemplate.opsForValue().set(key, value, expiration);
    }

    public void set(String key, Object value, long expiration) {
        redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
    }

    public void setIfAbsent(String key, Object value, Duration expiration) {
        redisTemplate.opsForValue().setIfAbsent(key, value, expiration);
    }

    public void setIfAbsent(String key, Object value, long expiration) {
        redisTemplate.opsForValue().setIfAbsent(key, value, expiration, TimeUnit.SECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    public Long del(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Boolean expire(String key, Duration expiration) {
        return redisTemplate.expire(key, expiration);
    }

    public Boolean expire(String key, long expiration) {
        return redisTemplate.expire(key, expiration, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public Long decr(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /*************************************** hash操作 ****************************************/

    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public void hMultiGet(String key, Collection<Object> hashKeys) {
        redisTemplate.opsForHash().multiGet(key, hashKeys);
    }

    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public void hSetIfAbsent(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().putIfAbsent(key, hashKey, value);
    }

    public void hMultiSet(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void hDel(String key, Object... hashKey) {
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    public Boolean hHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    public Long hIncr(String key, String hashKey, Long delta) {
        return redisTemplate.opsForHash().increment(key, hashKey, delta);
    }

    /*************************************** list操作 ****************************************/

    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public Long lSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public Long lLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long lLeftPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().leftPushIfPresent(key, value);
    }

    public Long lLeftPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    public Long lLeftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    public Object lLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object lLeftPop(String key, long count) {
        return redisTemplate.opsForList().leftPop(key, count);
    }

    public Long lRightPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long lRightPushIfPresent(String key, Object value) {
        return redisTemplate.opsForList().rightPushIfPresent(key, value);
    }

    public Long lRightPushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public Long lRightPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public Object lRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public Object lRightPop(String key, long count) {
        return redisTemplate.opsForList().rightPop(key, count);
    }

    public Long lRemove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /*************************************** set操作 ****************************************/

    public Long sAdd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    public Long sRemove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    public Set<Object> sMember(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /*************************************** zset操作 ****************************************/

    public Boolean zSetAdd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    public Boolean zSetAddIfAbsent(String key, Object value, double score) {
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score);
    }

    public Long zSetRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    public Double zSetIncrScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    public Set<Object> zSetRangeByScore(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScore(key, minScore, maxScore);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zSetRangeByScoreWithScores(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().rangeByScoreWithScores(key, minScore, maxScore);
    }

    public Set<Object> zSetReverseRangeByScore(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, minScore, maxScore);
    }

    public Set<ZSetOperations.TypedTuple<Object>> zSetReverseRangeByScoreWithScores(String key, double minScore, double maxScore) {
        return redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, minScore, maxScore);
    }

    public Long zSetCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    public Long zSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public Double zSetScore(String key, Object obj) {
        return redisTemplate.opsForZSet().score(key, obj);
    }

    public List<Double> zSetScore(String key, Object... objs) {
        return redisTemplate.opsForZSet().score(key, objs);
    }

    public ZSetOperations.TypedTuple<Object> zSetPopMin(String key) {
        return redisTemplate.opsForZSet().popMin(key);
    }

    public ZSetOperations.TypedTuple<Object> zSetPopMax(String key) {
        return redisTemplate.opsForZSet().popMax(key);
    }

    /*************************************** script操作 ****************************************/

    public <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
        return redisTemplate.execute(script, keys, args);
    }

}