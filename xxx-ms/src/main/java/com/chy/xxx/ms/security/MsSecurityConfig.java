package com.chy.xxx.ms.security;

import com.chy.xxx.ms.properties.MsSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * 后台security配置
 *
 * @author chy
 */
@Configuration
public class MsSecurityConfig {

    @Resource
    private MsSecurityProperties msSecurityProperties;
    @Resource
    private MsAccessDeniedHandler msAccessDeniedHandler;
    @Resource
    private MsAuthenticationEntryPoint msAuthenticationEntryPoint;
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Resource
    private DynamicSecurityFilter dynamicSecurityFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity.authorizeRequests();

        //OPTIONS请求直接放行
        registry.antMatchers(HttpMethod.OPTIONS).permitAll();
        //白名单资源直接放行
        for (String url : msSecurityProperties.getIgnoreUrls()) {
            registry.antMatchers(url).permitAll();
        }

        //其它请求都需要身份认证
        registry.and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                //关闭跨站请求防护及不使用session
                .and()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //使用自定义的权限校验异常处理
                .and()
                .exceptionHandling()
                .accessDeniedHandler(msAccessDeniedHandler)
                .authenticationEntryPoint(msAuthenticationEntryPoint)
                //添加jwt登录认证过滤器，校验用户登录状态
                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //添加动态权限校验过滤器，校验用户是否具有当前访问资源的权限
        registry.and().addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);

        return httpSecurity.build();
    }

}