package com.chy.xxx.ms.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;

import java.util.List;

/**
 * 系统资源表
 *
 * @author chy
 */
public interface SysResourceDao extends BaseMapper<SysResourcePo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysResourcePo>
     */
    List<SysResourcePo> listByQo(SysResourceQo qo);

}




