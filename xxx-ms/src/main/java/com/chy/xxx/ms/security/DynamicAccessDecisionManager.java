package com.chy.xxx.ms.security;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 动态权限决策管理器，用于校验用户是否有当前请求资源的访问权限
 *
 * @author chy
 */
@Component
public class DynamicAccessDecisionManager implements AccessDecisionManager {

    /**
     * 校验用户是否有当前请求资源的访问权限
     *
     * @param authentication 当前用户具有的权限
     * @param object 当前访问的目标资源
     * @param configAttributes 当前请求资源的访问规则
     * @throws AccessDeniedException 无访问权限异常
     * @throws InsufficientAuthenticationException 用户信任程度不够异常
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //当前资源未配置访问规则时直接放行
        if (CollectionUtils.isEmpty(configAttributes)) {
            return;
        }
        //遍历当前资源的所有访问规则
        for (ConfigAttribute configAttribute : configAttributes) {
            String needAuthority = configAttribute.getAttribute();
            //当前用户具有的权限中，只要有1个满足当前资源所需的访问规则就放行
            //eg.当前资源为/v1/system/users/info，配置了2个访问规则/v1/system/**、/v1/system/users/**，当前用户只要满足其中一个访问规则就可以放行
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                if (StringUtils.equals(needAuthority, grantedAuthority.getAuthority())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("没有访问权限");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}