package com.chy.xxx.ms.modules.system.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 系统角色分页查询响应信息
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统角色分页查询响应信息")
public class SysRolePageRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键")
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 状态：0正常，1禁用
     */
    @ApiModelProperty(value = "状态：0正常，1禁用")
    private Integer status;

    /**
     * 包含的系统用户列表
     */
    @ApiModelProperty(value = "包含的系统用户列表")
    private List<SysUserRespVo> sysUserList;

}