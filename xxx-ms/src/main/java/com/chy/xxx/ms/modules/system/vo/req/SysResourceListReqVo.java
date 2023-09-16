package com.chy.xxx.ms.modules.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 系统资源查询请求参数
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统资源查询请求参数")
public class SysResourceListReqVo {

    /**
     * 资源类型列表：1菜单，2操作/按钮，3接口
     */
    @ApiModelProperty(value = "资源类型列表：1菜单，2操作/按钮，3接口")
    private List<Integer> resTypes;

    /**
     * 资源状态：0正常，1禁用
     */
    @ApiModelProperty(value = "资源状态：0正常，1禁用")
    private Integer status;

}
