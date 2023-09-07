package com.chy.xxx.ms.modules.system.service.business;

import com.chy.xxx.ms.modules.system.vo.req.SysRoleAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleGrantPermissionReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRolePageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRoleRespVo;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;

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
    CommonResp<CommonPage<SysRoleRespVo>> page(SysRolePageReqVo reqVo);

    /**
     * 系统角色授权
     *
     * @param reqVo 请求参数
     * @return CommonResp<Void>
     */
    CommonResp<Void> grantPermission(SysRoleGrantPermissionReqVo reqVo);

}
