package com.chy.xxx.ms.modules.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统角色资源保存请求参数
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统角色资源保存请求参数")
public class SysRoleResourceSaveReqVo {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(value = "角色id", required = true)
    private Long roleId;

    /**
     * 资源id列表
     */
    @NotEmpty(message = "资源id列表不能为空")
    @ApiModelProperty(value = "资源id列表", required = true)
    private List<Long> resIds;

}
