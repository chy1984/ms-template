package com.chy.xxx.ms.security;

import com.chy.xxx.ms.modules.system.bo.SysUserResourceBo;
import com.chy.xxx.ms.modules.system.cache.SysUserResourceCacheService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUserResourceBo sysUserResourceBo = sysUserResourceCacheService.getUseResourceCache(username);
        return SysUserDetails.builder()
                .username(sysUserResourceBo.getUsername())
                .password(sysUserResourceBo.getPassword())
                .status(sysUserResourceBo.getStatus())
                .interfaceList(sysUserResourceBo.getInterfaceList())
                .build();
    }

}
