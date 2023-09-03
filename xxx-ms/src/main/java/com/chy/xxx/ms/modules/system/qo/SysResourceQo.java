package com.chy.xxx.ms.modules.system.qo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 系统资源Qo
 *
 * @author chy
 */
@Data
@Builder
public class SysResourceQo {

    /**
     * id列表
     */
    private List<Long> ids;

    /**
     * 资源类型
     */
    private Integer resType;

    /**
     * 资源url
     */
    private String resUrl;

    /**
     * 资源请求方式：GET、POST、PUT、DELETE
     */
    private String resReqMethod;

    /**
     * 资源状态
     */
    private Integer status;

    /**
     * 上级资源id
     */
    private Long parentId;

}
