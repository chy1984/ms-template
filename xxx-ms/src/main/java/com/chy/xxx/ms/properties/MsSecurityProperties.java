package com.chy.xxx.ms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 后台鉴权属性配置
 *
 * @author chy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security")
public class MsSecurityProperties {

    /**
     * 白名单url，直接放行
     */
    private String[] ignoreUrls;

}
