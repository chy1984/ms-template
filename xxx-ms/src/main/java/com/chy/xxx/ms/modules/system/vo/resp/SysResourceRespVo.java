package com.chy.xxx.ms.modules.system.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统资源响应信息
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("系统资源响应信息")
public class SysResourceRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键")
    private Long id;

    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    private String resName;

    /**
     * 资源类型：1菜单，2操作/按钮，3接口
     */
    @ApiModelProperty(value = "资源类型：1菜单，2操作/按钮，3接口")
    private Integer resType;

    /**
     * 资源图标
     */
    @ApiModelProperty(value = "资源图标")
    private String resIcon;

    /**
     * 资源url
     */
    @ApiModelProperty(value = "资源url")
    private String resUrl;

    /**
     * 资源请求方式：GET、POST、PUT、PATCH、DELETE
     */
    @ApiModelProperty(value = "资源请求方式：GET、POST、PUT、PATCH、DELETE")
    private String resReqMethod;

    /**
     * 同级别排序号，值越小展示越靠前
     */
    @ApiModelProperty(value = "同级别排序号，值越小展示越靠前")
    private Integer seq;

    /**
     * 状态：0正常，1禁用
     */
    @ApiModelProperty(value = "状态：0正常，1禁用")
    private Integer status;

    /**
     * 上级资源id
     */
    @ApiModelProperty(value = "上级资源id")
    private Long parentId;

}