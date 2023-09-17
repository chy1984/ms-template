package com.chy.xxx.ms.security;

import com.chy.xxx.ms.enums.MsErrorCodeEnum;
import com.chy.xxx.ms.exception.ServiceRuntimeException;
import com.chy.xxx.ms.modules.system.bo.SysUserResourceBo;
import com.chy.xxx.ms.modules.system.cache.SysUserResourceCacheService;
import com.chy.xxx.ms.modules.system.enums.SysUserStatusEnum;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * spring security UserDetailsService实现
 *
 * @author chy
 */
@Service
public class SysUserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserResourceCacheService sysUserResourceCacheService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUserResourceBo sysUserResourceBo = sysUserResourceCacheService.getUseResourceCache(username);
        Integer status = sysUserResourceBo.getStatus();
        if (SysUserStatusEnum.NOT_EXIST.getStatus().equals(status)) {
            throw new ServiceRuntimeException(MsErrorCodeEnum.SYS_USER_NOT_EXIST, "username=" + username);
        }
        if (SysUserStatusEnum.DISABLED.getStatus().equals(status)) {
            throw new ServiceRuntimeException(MsErrorCodeEnum.SYS_USER_DISABLED, "username=" + username);
        }
        return SysUserDetails.builder()
                .username(sysUserResourceBo.getUsername())
                .password(sysUserResourceBo.getPassword())
                .status(status)
                .interfaceList(sysUserResourceBo.getInterfaceList())
                .build();
    }

}
