package com.chy.xxx.ms.modules.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * 系统用户更新请求参数
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统用户更新请求参数")
public class SysUserUpdateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @NotNull(message = "主键id不能为空")
    @ApiModelProperty(value = "主键id", required = true)
    private Long id;

    /**
     * 手机号
     */
    @Pattern(regexp = "^[1][3-9][0-9]{9}$", message = "手机号为空或格式错误")
    @ApiModelProperty(value = "手机号", required = true)
    private String tel;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式错误")
    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    /**
     * 用户状态：0正常，1禁用
     */
    @NotNull(message = "用户状态不能为空")
    @ApiModelProperty(value = "用户状态：0正常，1禁用", required = true)
    private Integer status;

    /**
     * 角色id列表
     */
    @NotEmpty(message = "角色id列表不能为空")
    @ApiModelProperty(value = "角色id列表", required = true)
    private List<Long> roleIds;

}
