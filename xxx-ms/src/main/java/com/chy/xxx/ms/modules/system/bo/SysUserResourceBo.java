package com.chy.xxx.ms.modules.system.bo;

import com.chy.xxx.ms.modules.system.enums.SysResourceTypeEnum;
import com.chy.xxx.ms.modules.system.enums.SysUserStatusEnum;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 系统用户资源信息Bo
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserResourceBo implements Serializable {

    private static final long serialVersionUID = -6570053350017367564L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户状态：-1不存在，0正常，1禁用
     */
    private Integer status;

    /**
     * 有权限的菜单列表
     */
    private List<SysResourcePo> menuList;

    /**
     * 有权限的操作/按钮列表
     */
    private List<SysResourcePo> operationList;

    /**
     * 有权限的接口列表
     */
    private List<SysResourcePo> interfaceList;

    /**
     * 构建用户不存在的SysUserResourceBo
     *
     * @param username 用户名
     * @return SysUserResourceBo
     */
    public static SysUserResourceBo buildUserNotExist(String username) {
        return SysUserResourceBo.builder()
                .username(username)
                .status(SysUserStatusEnum.NOT_EXIST.getStatus())
                .realName(StringUtils.EMPTY)
                .password(StringUtils.EMPTY)
                .menuList(Collections.emptyList())
                .operationList(Collections.emptyList())
                .interfaceList(Collections.emptyList())
                .build();
    }

    /**
     * 构建用户无资源的SysUserResourceBo
     *
     * @param sysUserPo 用户信息
     * @return SysUserResourceBo
     */
    public static SysUserResourceBo buildUserNoResources(SysUserPo sysUserPo) {
        return SysUserResourceBo.builder()
                .username(sysUserPo.getUsername())
                .realName(sysUserPo.getRealName())
                .password(sysUserPo.getPassword())
                .status(sysUserPo.getStatus())
                .menuList(Collections.emptyList())
                .operationList(Collections.emptyList())
                .interfaceList(Collections.emptyList())
                .build();
    }

    /**
     * 构建用户有资源的SysUserResourceBo
     *
     * @param sysUserPo  用户信息
     * @param resTypeMap 资源类型map
     * @return SysUserResourceBo
     */
    public static SysUserResourceBo buildUserWithResources(SysUserPo sysUserPo, Map<Integer, List<SysResourcePo>> resTypeMap) {
        return SysUserResourceBo.builder()
                .username(sysUserPo.getUsername())
                .realName(sysUserPo.getRealName())
                .password(sysUserPo.getPassword())
                .status(sysUserPo.getStatus())
                .menuList(resTypeMap.getOrDefault(SysResourceTypeEnum.MENU.getResType(), Collections.emptyList()))
                .operationList(resTypeMap.getOrDefault(SysResourceTypeEnum.OPERATION.getResType(), Collections.emptyList()))
                .interfaceList(resTypeMap.getOrDefault(SysResourceTypeEnum.INTERFACE.getResType(), Collections.emptyList()))
                .build();
    }

}
