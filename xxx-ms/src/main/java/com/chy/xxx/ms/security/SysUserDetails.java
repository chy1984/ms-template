package com.chy.xxx.ms.security;

import com.chy.xxx.ms.modules.system.enums.SysUserStatusEnum;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * spring security需要的用户详情
 *
 * @author chy
 */
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDetails implements UserDetails {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户状态：-1不存在，0正常，1禁用
     */
    private Integer status;

    /**
     * 有权限的接口列表
     */
    private List<SysResourcePo> interfaceList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //返回当前用户具有的资源
        return this.interfaceList.stream()
                .map(resourcePo -> new SimpleGrantedAuthority(resourcePo.getId() + MsSecurityConstant.RES_SEPARATOR + resourcePo.getResName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return SysUserStatusEnum.NORMAL.getStatus().equals(status);
    }

    public Integer getStatus() {
        return this.status;
    }

    public List<SysResourcePo> getInterfaceList() {
        return this.interfaceList;
    }

}