package com.chy.xxx.ms.modules.system.enums;

import com.chy.xxx.ms.enums.MsErrorCodeEnum;
import com.chy.xxx.ms.exception.RtBizAssert;
import com.chy.xxx.ms.exception.ServiceRuntimeException;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
     * 资源类型code
     */
    private final Integer resType;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据 resType 获取枚举
     *
     * @param resType 资源类型code
     * @return SysResourceTypeEnum
     */
    public static SysResourceTypeEnum getEnumByResType(Integer resType) {
        for (SysResourceTypeEnum resourceTypeEnum : values()) {
            if (resourceTypeEnum.resType.equals(resType)) {
                return resourceTypeEnum;
            }
        }
        throw new ServiceRuntimeException(MsErrorCodeEnum.SYS_RESOURCE_TYPE_ERROR, "resType=" + resType);
    }

    public static final ImmutableMap<Integer, List<Integer>> ALLOW_PARENT_RES_MAPPING = ImmutableMap.<Integer, List<Integer>>builder()
            .put(MENU.resType, Arrays.asList(MENU.resType, null, 0))
            .put(OPERATION.resType, Collections.singletonList(MENU.resType))
            .put(INTERFACE.resType, Collections.singletonList(OPERATION.resType))
            .build();

    public static boolean validateParentResMapping(Integer resType, Integer parentResType) {
        RtBizAssert.assertNotNull(resType, "resType不能为空");
        List<Integer> allowParentResTypeList = ALLOW_PARENT_RES_MAPPING.get(resType);
        RtBizAssert.assertNotNull(allowParentResTypeList, "不合法的resType：" + resType);
        return allowParentResTypeList.contains(parentResType);
    }

}
