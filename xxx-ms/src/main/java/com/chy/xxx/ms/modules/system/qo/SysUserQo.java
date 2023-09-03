package com.chy.xxx.ms.modules.system.qo;

import lombok.Builder;
import lombok.Data;

/**
 * 系统用户Qo
 *
 * @author chy
 */
@Data
@Builder
public class SysUserQo {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态：0正常，1禁用
     */
    private Integer status;

}
