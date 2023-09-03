package com.chy.xxx.ms.modules.system.service.db.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chy.xxx.ms.modules.system.dao.SysUserDao;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.modules.system.qo.SysUserQo;
import com.chy.xxx.ms.modules.system.service.db.SysUserDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户DbService实现
 *
 * @author chy
 */
@Service
public class SysUserDbServiceImpl extends ServiceImpl<SysUserDao, SysUserPo> implements SysUserDbService {

    @Resource
    private SysUserDao sysUserDao;

    @Override
    public List<SysUserPo> listByQo(SysUserQo qo) {
        return sysUserDao.listByQo(qo);
    }

}




