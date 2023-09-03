package com.chy.xxx.ms.modules.system.service.db;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.modules.system.qo.SysUserQo;

import java.util.List;

/**
 * 系统用户DbService
 *
 * @author chy
 */
public interface SysUserDbService extends IService<SysUserPo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysUserPo>
     */
    List<SysUserPo> listByQo(SysUserQo qo);

}
