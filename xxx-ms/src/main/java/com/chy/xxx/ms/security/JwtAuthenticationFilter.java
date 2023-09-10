package com.chy.xxx.ms.security;

import com.chy.xxx.ms.component.JwtTokenService;
import com.chy.xxx.ms.enums.ErrorCodeEnum;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.properties.JwtTokenProperties;
import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.response.CommonResp;
import com.chy.xxx.ms.util.HttpUtil;
import com.chy.xxx.ms.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt登录认证过滤器，用于校验用户的登录状态
 *
 * @author chy
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtTokenService jwtTokenService;
    @Resource
    private JwtTokenProperties jwtTokenProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String tokenHeaderValue = request.getHeader(jwtTokenProperties.getRequestHeader());
        if (tokenHeaderValue != null && tokenHeaderValue.startsWith(jwtTokenProperties.getPrefix())) {
            String token = tokenHeaderValue.substring(jwtTokenProperties.getPrefix().length());
            SysUserPo sysUserPo = jwtTokenService.getUserInfoFromToken(token);
            log.info("jwt登录状态校验通过：token={},sysUserPo={}", token, JacksonUtil.toJsonStr(sysUserPo));

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = sysUserPo.getUsername();
                UserDetails userDetails;
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException e) {
                    log.error("【加载系统用户详细信息】用户不存在，username={}", username, e);
                    HttpUtil.flushResponse(response, CommonResp.fail(ErrorCodeEnum.SYS_USER_NOT_EXIST));
                    return;
                } catch (Exception e) {
                    log.error("【加载系统用户详细信息】执行异常，username={}", username, e);
                    HttpUtil.flushResponse(response, CommonResp.fail(ErrorCodeEnum.UNKNOWN_ERROR));
                    return;
                }

                //SecurityContext设置对应的authentication才算通过jwt登录状态校验，才会继续走后续的校验流程，否则系统会认为未通过jwt登录状态校验
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                //设置请求上下文中的用户信息
                RequestContextHolder.setUserInfo(username, sysUserPo.getRealName());
            }
        }
        chain.doFilter(request, response);
    }

}
