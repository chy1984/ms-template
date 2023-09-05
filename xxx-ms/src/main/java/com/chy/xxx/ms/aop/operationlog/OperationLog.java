package com.chy.xxx.ms.aop.operationlog;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author chy
 */
@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperationLog {

    /**
     * 日志标题
     *
     * @return String
     */
    @AliasFor("logTitle")
    String value() default "";

    /**
     * 日志标题
     *
     * @return String
     */
    @AliasFor("value")
    String logTitle() default "";

    /**
     * 是否保存响应数据。如果响应数据很庞大、没必要记录，可以置为false
     *
     * @return boolean
     */
    boolean saveRespData() default true;

}
