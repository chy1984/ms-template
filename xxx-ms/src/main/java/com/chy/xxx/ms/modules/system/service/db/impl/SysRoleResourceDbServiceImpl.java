package com.chy.xxx.ms.modules.system.service.db.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chy.xxx.ms.modules.system.dao.SysRoleResourceDao;
import com.chy.xxx.ms.modules.system.po.SysRoleResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleResourceQo;
import com.chy.xxx.ms.modules.system.service.db.SysRoleResourceDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色资源关联DbService实现
 *
 * @author chy
 */
@Service
public class SysRoleResourceDbServiceImpl extends ServiceImpl<SysRoleResourceDao, SysRoleResourcePo> implements SysRoleResourceDbService {

    @Resource
    private SysRoleResourceDao sysRoleResourceDao;

    @Override
    public List<SysRoleResourcePo> listByQo(SysRoleResourceQo qo){
        return sysRoleResourceDao.listByQo(qo);
    }

}




