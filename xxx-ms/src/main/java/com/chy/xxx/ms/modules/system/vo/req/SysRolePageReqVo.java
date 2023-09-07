package com.chy.xxx.ms.modules.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统角色分页查询请求参数
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统角色分页查询请求参数")
public class SysRolePageReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 当前页码
     */
    @NotNull(message = "当前页码不能为空")
    @ApiModelProperty(value = "当前页码", required = true)
    private Integer pageNum;

    /**
     * 每页记录数
     */
    @NotNull(message = "每页记录数不能为空")
    @ApiModelProperty(value = "每页记录数", required = true)
    private Integer pageSize;

}
