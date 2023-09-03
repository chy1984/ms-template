package com.chy.xxx.ms.modules.system.service.db;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chy.xxx.ms.modules.system.po.SysRoleResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleResourceQo;

import java.util.List;

/**
 * 系统角色资源关联DbService
 *
 * @author chy
 */
public interface SysRoleResourceDbService extends IService<SysRoleResourcePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysRoleResourcePo>
     */
    List<SysRoleResourcePo> listByQo(SysRoleResourceQo qo);

}
