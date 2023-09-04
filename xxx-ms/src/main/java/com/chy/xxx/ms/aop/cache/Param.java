package com.chy.xxx.ms.aop.cache;

import java.lang.annotation.*;

/**
 * 缓存key参数绑定注解
 *
 * @author chy
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 绑定的缓存key参数
     */
    String value();

}
