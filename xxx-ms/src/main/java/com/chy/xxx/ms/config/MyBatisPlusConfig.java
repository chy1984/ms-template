package com.chy.xxx.ms.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus配置
 *
 * @author chy
 */
@Configuration
@MapperScan(basePackages = {"com.chy.xxx.ms.modules.**.dao"})
public class MyBatisPlusConfig {

}
