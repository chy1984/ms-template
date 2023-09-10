package com.chy.xxx.ms.modules.system.service.business;

import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.response.CommonResp;

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
     * @return CommonResp<Void>
     */
    CommonResp<Void> add(SysResourceAddReqVo reqVo);

    /**
     * 更新资源信息
     *
     * @param reqVo 请求参数
     * @return CommonResp<Void>
     */
    CommonResp<Void> update(SysResourceUpdateReqVo reqVo);

    /**
     * 删除系统资源
     *
     * @param id 主键id
     * @return CommonResp<Void>
     */
    CommonResp<Void> delete(Long id);

    /**
     * 查询系统资源
     *
     * @param reqVo 请求参数
     * @return CommonResp<List<SysResourceRespVo>>
     */
    CommonResp<List<SysResourceRespVo>> list(SysResourceListReqVo reqVo);

}
