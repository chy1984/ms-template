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
     * @param authentication   当前用户具有的权限
     * @param object           当前访问的目标资源
     * @param configAttributes 当前请求资源所有的访问规则
     * @throws AccessDeniedException               无访问权限异常
     * @throws InsufficientAuthenticationException 用户信任程度不够异常
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //如果当前资源未纳入资源管理（未添加到资源管理，或资源状态不是生效中），则直接放行
        if (CollectionUtils.isEmpty(configAttributes)) {
            return;
        }

        /*
          遍历当前资源所支持的所有访问规则，这些规则是or的关系，用户满足其中一个就放行。示例：当前访问的资源 查询用户信息 GET /v1/system/users/info
         （1）定义了多个请求方式+url相同的接口：GET /v1/system/users/info，resId不同，只要拥有其中一个即可访问
         （2）定义了多个请求方式相同、url不同的访问规则：GET /v1/system/users/info、GET /v1/system/users/*、GET /v1/system/**，只要拥有其中一个即可访问
         */
        for (ConfigAttribute configAttribute : configAttributes) {
            String needAuthority = configAttribute.getAttribute();
            //遍历当前用户有权限的访问规则，只要其中有1个满足就放行
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
