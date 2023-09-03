package com.chy.xxx.ms.modules.system.service.db.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chy.xxx.ms.modules.system.dao.SysUserRoleDao;
import com.chy.xxx.ms.modules.system.po.SysUserRolePo;
import com.chy.xxx.ms.modules.system.qo.SysUserRoleQo;
import com.chy.xxx.ms.modules.system.service.db.SysUserRoleDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户角色关联DbService实现
 *
 * @author chy
 */
@Service
public class SysUserRoleDbServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRolePo> implements SysUserRoleDbService {

    @Resource
    private SysUserRoleDao sysUserRoleDao;

    @Override
    public List<SysUserRolePo> listByQo(SysUserRoleQo qo) {
        return sysUserRoleDao.listByQo(qo);
    }

    @Override
    public void deleteByUserId(Long userId) {
        sysUserRoleDao.deleteByUserId(userId);
    }

}




