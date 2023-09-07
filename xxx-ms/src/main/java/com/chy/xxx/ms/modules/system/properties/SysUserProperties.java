package com.chy.xxx.ms.modules.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 系统用户属性配置
 *
 * @author chy
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system.user")
public class SysUserProperties {

    /**
     * 默认密码
     */
    private String defaultPassword;
    
}
