package com.chy.xxx.ms.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chy.xxx.ms.modules.system.po.SysRoleResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleResourceQo;

import java.util.List;

/**
 * 系统角色资源关联表
 *
 * @author chy
 */
public interface SysRoleResourceDao extends BaseMapper<SysRoleResourcePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysRoleResourcePo>
     */
    List<SysRoleResourcePo> listByQo(SysRoleResourceQo qo);

}




