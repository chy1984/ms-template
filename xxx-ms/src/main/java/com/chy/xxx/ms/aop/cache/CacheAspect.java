package com.chy.xxx.ms.aop.cache;

import com.chy.xxx.ms.cache.CacheService;
import com.chy.xxx.ms.exception.RtBizAssert;
import com.chy.xxx.ms.properties.CacheProperties;
import com.chy.xxx.ms.util.AopUtil;
import com.chy.xxx.ms.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 缓存切面
 *
 * @author chy
 */
@Slf4j
@Aspect
@Component
public class CacheAspect {

    @Resource(name = "redisCacheService")
    private CacheService cacheService;
    @Resource
    private CacheProperties cacheProperties;

    /**
     * redis缓存的null值<br/>
     * 缓存时null值会转换为字符串"null"存储，从缓存获取时"null"会被还原为null
     */
    private static final String REDIS_NULL_VALUE = "null";

    @Pointcut("@annotation(com.chy.xxx.ms.aop.cache.Cacheable)")
    public void cacheablePointcut() {
        //@Cacheable切入点
    }

    @Pointcut("@annotation(com.chy.xxx.ms.aop.cache.RemoveCache)")
    public void removeCachePointcut() {
        //@RemoveCache切入点
    }

    @Around("cacheablePointcut()")
    public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
        Boolean enable = cacheProperties.getEnable();
        if (BooleanUtils.isNotTrue(enable)) {
            return joinPoint.proceed();
        }

        //组装cacheKey
        Method sourceMethod = AopUtil.getSourceMethod(joinPoint);
        Cacheable cacheable = sourceMethod.getAnnotation(Cacheable.class);
        String cacheKey = this.getCacheKey(cacheable.cacheName(), cacheable.cacheParams(), sourceMethod, joinPoint.getArgs());

        //先查缓存，缓存中有则直接返回
        Object cacheValue = cacheService.getValue(cacheKey);
        log.debug("缓存查询结果cacheValue={}", cacheValue);
        if (cacheValue != null) {
            if (!REDIS_NULL_VALUE.equals(cacheValue)) {
                return cacheValue;
            }
            if (cacheable.cacheNullValue()) {
                return null;
            }
        }

        //否则执行目标方法，将结果写入缓存
        Object result = joinPoint.proceed();
        if (result != null || cacheable.cacheNullValue()) {
            cacheValue = Objects.isNull(result) ? REDIS_NULL_VALUE : result;
            cacheService.setCache(cacheKey, cacheValue, cacheable.expiration());
        }
        return result;
    }

    @After("removeCachePointcut()")
    public void remove(JoinPoint joinPoint) throws NoSuchMethodException {
        Boolean enable = cacheProperties.getEnable();
        if (BooleanUtils.isNotTrue(enable)) {
            return;
        }

        Method sourceMethod = AopUtil.getSourceMethod(joinPoint);
        RemoveCache removeCache = sourceMethod.getAnnotation(RemoveCache.class);
        String cacheKey = this.getCacheKey(removeCache.cacheName(), removeCache.cacheParams(), sourceMethod, joinPoint.getArgs());
        cacheService.removeCache(cacheKey);
    }

    private String getCacheKey(String cacheName, String[] cacheParams, Method method, Object[] args) {
        Map<String, Object> paramMap = this.parseParamAnnotations(method, args);
        String cacheParamStr = this.parseCacheParams(cacheParams, paramMap);

        //cacheKey格式：keyPrefix_cacheName_cacheParamStr
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(cacheProperties.getKeyPrefix())) {
            sb.append(cacheProperties.getKeyPrefix()).append("_");
        }
        sb.append(cacheName);
        if (StringUtils.isNotBlank(cacheParamStr)) {
            sb.append("_").append(cacheParamStr);
        }
        log.debug("拼接得到cacheKey={}", sb);
        return sb.toString();
    }

    /**
     * 解析cacheParams属性
     *
     * @param cacheParams 缓存注解的cacheParams属性值
     * @param paramMap    解析@Param得到的绑定参数map
     * @return String，cacheParams各个元素对应的值拼接而成的字符串
     */
    private String parseCacheParams(String[] cacheParams, Map<String, Object> paramMap) {
        if (ArrayUtils.isEmpty(cacheParams)) {
            return StringUtils.EMPTY;
        }

        StringBuilder sb = new StringBuilder();
        for (String cacheParam : cacheParams) {
            String[] fieldNameArr = cacheParam.split("\\.");
            Object obj = paramMap.get(fieldNameArr[0]);
            RtBizAssert.assertNotNull(obj, String.format("cacheParams中%s找不到对应的绑定参数，或绑定参数的值为null", cacheParam));
            if (fieldNameArr.length == 1) {
                sb.append("_").append(obj);
                continue;
            }
            for (int index = 1; index < fieldNameArr.length; index++) {
                Object fieldValue = ReflectionUtil.getFieldValue(obj, fieldNameArr[index]);
                sb.append("_").append(fieldValue);
                obj = fieldValue;
            }
        }
        //移除开头多余的_
        sb.deleteCharAt(0);
        log.debug("解析cacheParams得到cacheParamStr={}", sb);
        return sb.toString();
    }

    /**
     * 解析 @Param 缓存参数绑定注解
     *
     * @param method 切入点对应的方法
     * @param args   实参表
     * @return Map<String, Object>，key是@Param注解的value，value是对应的实参
     */
    private Map<String, Object> parseParamAnnotations(Method method, Object[] args) {
        //获取方法参数的注解列表，一个参数对应一个一维数组（该参数上标注的所有注解）
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, Object> map = new HashMap<>();
        int index = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Param) {
                    Param param = (Param) annotation;
                    map.put(param.value(), args[index]);
                }
            }
            index++;
        }
        return map;
    }

}
