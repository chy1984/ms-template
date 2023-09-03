package com.chy.xxx.ms.modules.system.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统角色状态枚举
 *
 * @author chy
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SysRoleStatusEnum {

    /**
     * 系统角色状态枚举实例
     */
    NORMAL(0, "正常"),
    DISABLE(1, "禁用"),
    ;

    /**
     * 状态code
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;

}
