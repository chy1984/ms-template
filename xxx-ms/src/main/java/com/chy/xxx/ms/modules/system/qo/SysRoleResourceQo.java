package com.chy.xxx.ms.modules.system.qo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 系统角色资源Qo
 *
 * @author chy
 */
@Data
@Builder
public class SysRoleResourceQo {

    /**
     * 角色id列表
     */
    private List<Long> roleIds;

}