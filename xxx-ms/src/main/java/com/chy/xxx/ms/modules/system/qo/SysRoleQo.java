package com.chy.xxx.ms.modules.system.qo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 系统角色Qo
 *
 * @author chy
 */
@Data
@Builder
public class SysRoleQo {

    /**
     * id列表
     */
    private List<Long> ids;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 状态
     */
    private Integer status;

}