package com.chy.xxx.ms.modules.system.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统资源状态枚举
 *
 * @author chy
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SysResourceStatusEnum {

    /**
     * 系统资源配置的是资源访问规则，正常表示该条访问规则是生效中的，禁用指的的是该条访问规则作废，并不是资源作废
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
