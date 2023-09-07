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
 * 系统角色更新请求参数
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统角色更新请求参数")
public class SysRoleUpdateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @NotNull(message = "主键id不能为空")
    @ApiModelProperty(value = "自增主键", required = true)
    private Long id;

    /**
     * 角色状态：0正常，1禁用
     */
    @NotNull(message = "角色状态不能为空")
    @ApiModelProperty(value = "角色状态：0正常，1禁用", required = true)
    private Integer status;

}