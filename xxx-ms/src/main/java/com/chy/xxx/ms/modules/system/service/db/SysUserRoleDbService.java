package com.chy.xxx.ms.modules.system.service.db;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chy.xxx.ms.modules.system.po.SysUserRolePo;
import com.chy.xxx.ms.modules.system.qo.SysUserRoleQo;

import java.util.List;

/**
 * 系统用户角色关联DbService
 *
 * @author chy
 */
public interface SysUserRoleDbService extends IService<SysUserRolePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysUserRolePo>
     */
    List<SysUserRolePo> listByQo(SysUserRoleQo qo);

    /**
     * 根据userId删除
     *
     * @param userId 用户id
     */
    void deleteByUserId(Long userId);

}
