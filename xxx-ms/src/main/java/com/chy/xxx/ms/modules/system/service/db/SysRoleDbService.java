package com.chy.xxx.ms.modules.system.service.db;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chy.xxx.ms.modules.system.po.SysRolePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleQo;

import java.util.List;

/**
 * 系统角色DbService
 *
 * @author chy
 */
public interface SysRoleDbService extends IService<SysRolePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysRolePo>
     */
    List<SysRolePo> listByQo(SysRoleQo qo);

}
