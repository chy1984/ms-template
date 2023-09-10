package com.chy.xxx.ms.modules.system.qo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 系统用户角色Qo
 *
 * @author chy
 */
@Data
@Builder
public class SysUserRoleQo {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户id列表
     */
    private List<Long> userIds;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 角色id列表
     */
    private List<Long> roleIds;

}
