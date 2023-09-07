package com.chy.xxx.ms.modules.system.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 系统资源更新请求参数
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统资源更新请求参数")
public class SysResourceUpdateReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @NotNull(message = "主键id不能为空")
    @ApiModelProperty(value = "自增主键", required = true)
    private Long id;

    /**
     * 资源名称
     */
    @NotBlank(message = "资源名称不能为空")
    @ApiModelProperty(value = "资源名称", required = true)
    private String resName;

    /**
     * 资源类型：1菜单，2操作/按钮，3接口
     */
    @NotNull(message = "资源类型不能为空")
    @ApiModelProperty(value = "资源类型：1菜单，2操作/按钮，3接口", required = true)
    private Integer resType;

    /**
     * 资源图标
     */
    @ApiModelProperty(value = "资源图标")
    private String resIcon;

    /**
     * 资源url
     */
    @NotBlank(message = "资源url不能为空")
    @ApiModelProperty(value = "资源url", required = true)
    private String resUrl;

    /**
     * 资源请求方式：GET、POST、PUT、DELETE
     */
    @ApiModelProperty(value = "资源请求方式：GET、POST、PUT、DELETE")
    private String resReqMethod;

    /**
     * 同级别排序号，值越小展示越靠前
     */
    @ApiModelProperty(value = "同级别排序号，值越小展示越靠前")
    private Integer seq;

    /**
     * 资源状态：0正常，1禁用
     */
    @NotNull(message = "资源状态不能为空")
    @ApiModelProperty(value = "资源状态：0正常，1禁用", required = true)
    private Integer status;

    /**
     * 上级资源id
     */
    @ApiModelProperty(value = "上级资源id")
    private Long parentId;

}