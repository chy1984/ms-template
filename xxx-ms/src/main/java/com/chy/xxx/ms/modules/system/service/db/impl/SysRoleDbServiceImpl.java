package com.chy.xxx.ms.modules.system.service.db.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chy.xxx.ms.modules.system.dao.SysRoleDao;
import com.chy.xxx.ms.modules.system.po.SysRolePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleQo;
import com.chy.xxx.ms.modules.system.service.db.SysRoleDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色DbService实现
 *
 * @author chy
 */
@Service
public class SysRoleDbServiceImpl extends ServiceImpl<SysRoleDao, SysRolePo> implements SysRoleDbService {

    @Resource
    private SysRoleDao sysRoleDao;

    @Override
    public List<SysRolePo> listByQo(SysRoleQo qo) {
        return sysRoleDao.listByQo(qo);
    }

}




