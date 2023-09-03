package com.chy.xxx.ms.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chy.xxx.ms.modules.system.po.SysUserRolePo;
import com.chy.xxx.ms.modules.system.qo.SysUserRoleQo;

import java.util.List;

/**
 * 系统用户角色关联表
 *
 * @author chy
 */
public interface SysUserRoleDao extends BaseMapper<SysUserRolePo> {

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
     * @return int
     */
    int deleteByUserId(Long userId);

}




