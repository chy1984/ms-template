package com.chy.xxx.ms.modules.system.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统用户状态枚举
 *
 * @author chy
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SysUserStatusEnum {

    /**
     * 系统用户状态枚举实例
     */
    NOT_EXIST(-1, "用户不存在"),
    NORMAL(0, "正常"),
    DISABLED(1, "禁用"),
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
