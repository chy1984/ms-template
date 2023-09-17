package com.chy.xxx.ms.modules.system.service.business;

import com.chy.xxx.ms.modules.system.vo.req.*;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRolePageRespVo;
import com.chy.xxx.ms.response.CommonPage;

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
     */
    void add(SysRoleAddReqVo reqVo);

    /**
     * 更新系统角色
     *
     * @param reqVo 请求参数
     */
    void update(SysRoleUpdateReqVo reqVo);

    /**
     * 删除系统角色
     *
     * @param id 主键id
     */
    void delete(Long id);

    /**
     * 分页查询系统角色
     *
     * @param reqVo 请求参数
     * @return CommonPage<SysRoleRespVo>
     */
    CommonPage<SysRolePageRespVo> page(SysRolePageReqVo reqVo);

    /**
     * 查询系统角色具有的资源列表
     *
     * @param reqVo 请求参数
     * @return List<SysResourceRespVo>
     */
    List<SysResourceRespVo> listRoleResources(SysRoleResourceListReqVo reqVo);

    /**
     * 保存系统角色资源配置
     *
     * @param reqVo 请求参数
     */
    void saveRoleResources(SysRoleResourceSaveReqVo reqVo);

}
