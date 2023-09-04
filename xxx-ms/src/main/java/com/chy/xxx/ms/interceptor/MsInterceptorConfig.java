package com.chy.xxx.ms.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 后台拦截器配置
 *
 * @author chy
 */
@Configuration
public class MsInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //请求上下文拦截器
        registry.addInterceptor(new RequestContextInterceptor())
                .addPathPatterns("/**")
                //执行顺序，默认0，浏览器->服务器时，值越小的越先执行，服务器->浏览器则相反
                .order(-1);
    }

}
