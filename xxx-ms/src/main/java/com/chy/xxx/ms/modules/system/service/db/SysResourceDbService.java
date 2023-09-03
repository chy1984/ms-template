package com.chy.xxx.ms.modules.system.service.db;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;

import java.util.List;

/**
 * 系统资源DbService
 *
 * @author chy
 */
public interface SysResourceDbService extends IService<SysResourcePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysResourcePo>
     */
    List<SysResourcePo> listByQo(SysResourceQo qo);

}
