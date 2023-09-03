package com.chy.xxx.ms.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chy.xxx.ms.modules.system.po.SysRolePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleQo;

import java.util.List;

/**
 * 系统角色表
 *
 * @author chy
 */
public interface SysRoleDao extends BaseMapper<SysRolePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysRolePo>
     */
    List<SysRolePo> listByQo(SysRoleQo qo);

}




