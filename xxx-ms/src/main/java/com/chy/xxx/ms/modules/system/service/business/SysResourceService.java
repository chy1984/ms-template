package com.chy.xxx.ms.modules.system.service.business;

import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;

import java.util.List;

/**
 * 系统资源业务service
 *
 * @author chy
 */
public interface SysResourceService {

    /**
     * 添加系统资源
     *
     * @param reqVo 请求参数
     */
    void add(SysResourceAddReqVo reqVo);

    /**
     * 更新资源信息
     *
     * @param reqVo 请求参数
     */
    void update(SysResourceUpdateReqVo reqVo);

    /**
     * 删除系统资源
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 查询系统资源
     *
     * @param reqVo 请求参数
     * @return List<SysResourceRespVo>
     */
    List<SysResourceRespVo> list(SysResourceListReqVo reqVo);

}
