package com.chy.xxx.ms.modules.system.service.tx;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chy.xxx.ms.modules.system.po.SysRoleResourcePo;
import com.chy.xxx.ms.modules.system.service.db.SysResourceDbService;
import com.chy.xxx.ms.modules.system.service.db.SysRoleResourceDbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 系统角色事务service
 *
 * @author chy
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysResourceTxService {

    @Resource
    private SysResourceDbService sysResourceDbService;
    @Resource
    private SysRoleResourceDbService sysRoleResourceDbService;

    /**
     * 删除资源
     *
     * @param resId 资源id
     */
    public void deleteResource(Long resId) {
        sysResourceDbService.removeById(resId);
        sysRoleResourceDbService.remove(new UpdateWrapper<>(SysRoleResourcePo.builder()
                .resId(resId)
                .build()));
    }

}
