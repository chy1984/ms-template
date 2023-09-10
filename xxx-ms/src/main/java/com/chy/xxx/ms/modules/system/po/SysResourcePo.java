package com.chy.xxx.ms.modules.system.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统资源表
 *
 * @author chy
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="t_sys_resource")
public class SysResourcePo implements Serializable {

    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 资源名称
     */
    private String resName;

    /**
     * 资源类型：1菜单，2操作/按钮，3接口
     */
    private Integer resType;

    /**
     * 资源图标
     */
    private String resIcon;

    /**
     * 资源url
     */
    private String resUrl;

    /**
     * 资源请求方式：GET、POST、PUT、PATCH、DELETE
     */
    private String resReqMethod;

    /**
     * 同级别排序号，值越小展示越靠前
     */
    private Integer seq;

    /**
     * 资源状态：0正常，1禁用
     */
    private Integer status;

    /**
     * 上级资源id
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}