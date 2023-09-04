package com.chy.xxx.ms.aop.cache;

import java.lang.annotation.*;

/**
 * 缓存注解
 *
 * @author chy
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * 缓存名称
     */
    String cacheName();

    /**
     * 缓存参数，可使用.点号访问对象的字段
     */
    String[] cacheParams() default {};

    /**
     * 是否缓存null值
     */
    boolean cacheNullValue() default true;

    /**
     * 缓存有效期，单位：秒，默认5分钟
     */
    int expiration() default 60 * 5;

}
