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

        //白名单资源直接放行
        registry.antMatchers(msSecurityProperties.getIgnoreUrls()).permitAll()
                //跨域的OPTIONS请求直接放行
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                //开启请求认证
                .and().authorizeRequests()
                //其它请求都需要身份认证
                .anyRequest().authenticated()
                //关闭跨站请求防护
                .and().csrf().disable()
                //不使用session维持会话状态
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //使用自定义的权限校验异常处理
                .and().exceptionHandling().authenticationEntryPoint(msAuthenticationEntryPoint).accessDeniedHandler(msAccessDeniedHandler)
                //添加jwt登录认证过滤器，校验用户登录状态
                .and().addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                //添加动态权限校验过滤器，校验用户是否具有当前访问资源的权限
                //dynamicSecurityFilter管理的资源是从资源管理中加载的，只加载生效中的资源。如果资源不在白名单内、不是跨域的options请求，且未纳入资源管理中（或资源状态不是生效中），
                //则只走上面的 jwtAuthenticationFilter 登录状态校验，登录了即可访问，比如：登出接口、修改当前用户密码接口。
                //参考：DynamicAccessDecisionManager#decide
                .addFilterBefore(dynamicSecurityFilter, FilterSecurityInterceptor.class);

        return httpSecurity.build();
    }

}
