package com.chy.xxx.ms.security;

import com.chy.xxx.ms.response.CommonResp;
import com.chy.xxx.ms.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
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
        HttpUtil.flushResponse(response, CommonResp.forbidden());
    }

}
