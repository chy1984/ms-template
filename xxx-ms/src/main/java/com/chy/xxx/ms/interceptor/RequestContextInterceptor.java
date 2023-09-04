package com.chy.xxx.ms.interceptor;

import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.util.IpUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求上下文拦截器
 *
 * @author chy
 */
public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求上下文设置客户端信息
        String clientIp = IpUtil.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        RequestContextHolder.setClientInfo(clientIp, userAgent);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空请求上下文
        RequestContextHolder.clear();
    }

}