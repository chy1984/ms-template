package com.chy.xxx.ms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 缓存属性配置
 *
 * @author chy
 */
@Data
@Component
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    /**
     * 是否启用缓存<br/>
     * 注意：开启缓存->关闭缓存->再次开启缓存，中间关闭缓存时不会维护/更新对应的缓存数据，缓存中可能有过时的脏数据。再次开启缓存之前尽量先清除缓存，缓存时间尽量不要设置太长。
     */
    private Boolean enable;

    /**
     * key前缀。如果多个项目共用缓存服务器，建议指定为项目名称，单词之间下划线连接
     */
    private String keyPrefix;

}
