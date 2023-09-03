package com.chy.xxx.ms.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.modules.system.qo.SysUserQo;

import java.util.List;

/**
 * 系统用户表
 *
 * @author chy
 */
public interface SysUserDao extends BaseMapper<SysUserPo> {

    /**
     * 按条件查询
     *
     * @param qo 查询条件
     * @return List<SysUserPo>
     */
    List<SysUserPo> listByQo(SysUserQo qo);

}




