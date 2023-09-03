package com.chy.xxx.ms.modules.system.service.db.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chy.xxx.ms.modules.system.dao.SysResourceDao;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;
import com.chy.xxx.ms.modules.system.service.db.SysResourceDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统资源DbService实现
 *
 * @author chy
 */
@Service
public class SysResourceDbServiceImpl extends ServiceImpl<SysResourceDao, SysResourcePo> implements SysResourceDbService {

    @Resource
    private SysResourceDao sysResourceDao;

    @Override
    public List<SysResourcePo> listByQo(SysResourceQo qo) {
        return sysResourceDao.listByQo(qo);
    }

}




