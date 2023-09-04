package com.chy.xxx.ms.aop.cache;

import java.lang.annotation.*;

/**
 * 清除缓存注解
 *
 * @author chy
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoveCache {

    /**
     * 缓存名称
     */
    String cacheName();

    /**
     * 缓存参数，可使用.点号访问对象的字段
     */
    String[] cacheParams() default {};

}
