package com.chy.xxx.ms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt token属性配置
 *
 * @author chy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.token")
public class JwtTokenProperties {

    /**
     * 携带token的请求头字段
     */
    private String requestHeader;

    /**
     * token前缀
     */
    private String prefix;

    /**
     * token加解密使用的密钥
     */
    private String secret;

    /**
     * token有效期，单位：秒
     */
    private Integer expiration;

    /**
     * 在过期前多久可以刷新token，单位：秒
     */
    private Integer refreshBeforeExpire;

    /**
     * 通知前端刷新token的响应头字段
     */
    private String refreshResponseHeader;

}
