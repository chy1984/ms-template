package com.chy.xxx.ms.security;

import com.chy.xxx.ms.response.CommonResp;
import com.chy.xxx.ms.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义未登录或登录过期时的响应数据
 *
 * @author chy
 */
@Slf4j
@Component
public class MsAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        HttpUtil.flushResponse(response, CommonResp.unauthorized());
    }

}
