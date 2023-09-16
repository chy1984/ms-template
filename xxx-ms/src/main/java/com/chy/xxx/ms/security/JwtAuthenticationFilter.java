package com.chy.xxx.ms.security;

import com.chy.xxx.ms.component.JwtTokenService;
import com.chy.xxx.ms.enums.MsErrorCodeEnum;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.properties.JwtTokenProperties;
import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.response.CommonResp;
import com.chy.xxx.ms.util.HttpUtil;
import com.chy.xxx.ms.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * 如果此方法抛出异常，或未设置用户凭证 让chain上后续的过滤器检测到没有用户凭证，会被外层捕获作为 AuthenticationException 抛出，进而被 MsAuthenticationEntryPoint 处理
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = jwtTokenService.getToken(request);
        if (StringUtils.isNotBlank(token)) {
            //如果token不合法、已过期，此处会抛出异常，外层捕获转而抛出 AuthenticationException
            SysUserPo sysUserPo = jwtTokenService.getUserInfoFromToken(token);
            log.info("jwt登录状态校验通过：token={},sysUserPo={}", token, JacksonUtil.toJsonStr(sysUserPo));

            //如果token即将过期，则设置一个flag通知前端刷新token
            if (Boolean.TRUE.equals(jwtTokenService.needRefreshToken(token).getLeft())) {
                String refreshResponseHeader = jwtTokenProperties.getRefreshResponseHeader();
                response.setHeader(refreshResponseHeader, "true");
                //浏览器默认只暴露 cache-control、content-type、expires、pragma 等少数响应头给前端，需要在
                response.setHeader("Access-Control-Expose-Headers", refreshResponseHeader);
            }

            //如果前面未设置用户凭证，则在此处设置
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String username = sysUserPo.getUsername();
                UserDetails userDetails;
                //此处 try...catch 返回指定的错误信息，与未通过jwt认证 MsAuthenticationEntryPoint 返回的错误信息作区分
                try {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException e) {
                    log.error("【加载系统用户详细信息】用户不存在，username={}", username, e);
                    HttpUtil.flushResponse(response, CommonResp.fail(MsErrorCodeEnum.SYS_USER_NOT_EXIST));
                    return;
                } catch (Exception e) {
                    log.error("【加载系统用户详细信息】执行异常，username={}", username, e);
                    HttpUtil.flushResponse(response, CommonResp.fail(MsErrorCodeEnum.UNKNOWN_ERROR));
                    return;
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            //设置请求上下文中的用户信息
            RequestContextHolder.setUserInfo(sysUserPo.getUsername(), sysUserPo.getRealName());
        }

        //如果token为空，则没有用户凭证 SecurityContext.authentication，chain上的后续过滤器检测到时会抛出 AuthenticationException
        chain.doFilter(request, response);
    }

}
