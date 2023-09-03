package com.chy.xxx.ms.modules.system.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统资源类型枚举
 *
 * @author chy
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SysResourceTypeEnum {

    /**
     * 系统资源类型枚举实例
     */
    MENU(1, "菜单"),
    OPERATION(2, "操作、按钮"),
    INTERFACE(3, "接口"),
    ;

    /**
     * 类型code
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

}
