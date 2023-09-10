package com.chy.xxx.ms.modules.system.service.business;

import com.chy.xxx.ms.modules.system.vo.req.SysRoleAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRolePageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleResourceSaveReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRolePageRespVo;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;

import java.util.List;

/**
 * 系统角色业务service
 *
 * @author chy
 */
public interface SysRoleService {

    /**
     * 添加系统角色
     *
     * @param reqVo 请求参数
     * @return CommonResp<Void>
     */
    CommonResp<Void> add(SysRoleAddReqVo reqVo);

    /**
     * 更新系统角色
     *
     * @param reqVo 请求参数
     * @return CommonResp<Void>
     */
    CommonResp<Void> update(SysRoleUpdateReqVo reqVo);

    /**
     * 删除系统角色
     *
     * @param id 主键id
     * @return CommonResp<Void>
     */
    CommonResp<Void> delete(Long id);

    /**
     * 分页查询系统角色
     *
     * @param reqVo 请求参数
     * @return CommonResp<CommonPage < SysRoleRespVo>>
     */
    CommonResp<CommonPage<SysRolePageRespVo>> page(SysRolePageReqVo reqVo);

    /**
     * 查询系统角色具有的资源列表
     *
     * @param reqVo 请求参数
     * @return CommonResp<List < SysResourceRespVo>>
     */
    CommonResp<List<SysResourceRespVo>> listRoleResources(SysRoleResourceListReqVo reqVo);

    /**
     * 保存系统角色资源配置
     *
     * @param reqVo 请求参数
     * @return CommonResp<Void>
     */
    CommonResp<Void> saveRoleResources(SysRoleResourceSaveReqVo reqVo);

}
