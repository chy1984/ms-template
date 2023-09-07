package com.chy.xxx.ms.security;

import com.chy.xxx.ms.response.CommonResp;
import com.chy.xxx.ms.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义没有权限访问时的处理器
 *
 * @author chy
 */
@Slf4j
@Component
public class MsAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //http状态码依然返回200，前端统一通过响应体中的错误码判断结果
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(JacksonUtil.toJsonStr(CommonResp.forbidden()));
        response.getWriter().flush();
    }

}
