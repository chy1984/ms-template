package com.chy.xxx.ms.modules.system.service.tx;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chy.xxx.ms.modules.system.po.SysRoleResourcePo;
import com.chy.xxx.ms.modules.system.po.SysUserRolePo;
import com.chy.xxx.ms.modules.system.service.db.SysResourceDbService;
import com.chy.xxx.ms.modules.system.service.db.SysRoleDbService;
import com.chy.xxx.ms.modules.system.service.db.SysRoleResourceDbService;
import com.chy.xxx.ms.modules.system.service.db.SysUserRoleDbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色事务service
 *
 * @author chy
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleTxService {

    @Resource
    private SysRoleDbService sysRoleDbService;
    @Resource
    private SysResourceDbService sysResourceDbService;
    @Resource
    private SysUserRoleDbService sysUserRoleDbService;
    @Resource
    private SysRoleResourceDbService sysRoleResourceDbService;

    /**
     * 角色授权
     *
     * @param roleId             角色id
     * @param sysRoleResourcePos 待插入的角色-资源关联记录列表
     */
    public void grantPermission(Long roleId, List<SysRoleResourcePo> sysRoleResourcePos) {
        sysRoleResourceDbService.remove(new UpdateWrapper<>(SysRoleResourcePo.builder()
                .roleId(roleId)
                .build()));
        sysRoleResourceDbService.saveBatch(sysRoleResourcePos);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色id
     */
    public void deleteRole(Long roleId) {
        sysRoleDbService.removeById(roleId);
        sysUserRoleDbService.remove(new UpdateWrapper<>(SysUserRolePo.builder()
                .roleId(roleId)
                .build()
        ));
        sysRoleResourceDbService.remove(new UpdateWrapper<>(SysRoleResourcePo.builder()
                .roleId(roleId)
                .build()));
    }

}
