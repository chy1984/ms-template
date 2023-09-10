package com.chy.xxx.ms.modules.system.service.tx;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.modules.system.po.SysUserRolePo;
import com.chy.xxx.ms.modules.system.service.db.SysUserDbService;
import com.chy.xxx.ms.modules.system.service.db.SysUserRoleDbService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统用户事务service
 *
 * @author chy
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserTxService {

    @Resource
    private SysUserDbService sysUserDbService;
    @Resource
    private SysUserRoleDbService sysUserRoleDbService;

    /**
     * 添加系统用户
     *
     * @param sysUserPo      待插入的系统用户信息
     * @param sysUserRolePos 待插入的系统用户-角色关联记录列表
     */
    public void addUser(SysUserPo sysUserPo, List<SysUserRolePo> sysUserRolePos) {
        sysUserDbService.save(sysUserPo);
        Long userId = sysUserPo.getId();
        sysUserRolePos.forEach(sysUserRolePo -> sysUserRolePo.setUserId(userId));
        sysUserRoleDbService.saveBatch(sysUserRolePos);
    }

    /**
     * 更新系统用户
     *
     * @param sysUserUpdatePo      待更新的系统用户信息
     * @param sysUserRoleInsertPos 待插入的系统用户-角色关联记录
     */
    public void updateUser(SysUserPo sysUserUpdatePo, List<SysUserRolePo> sysUserRoleInsertPos) {
        sysUserDbService.updateById(sysUserUpdatePo);
        sysUserRoleDbService.deleteByUserId(sysUserUpdatePo.getId());
        sysUserRoleDbService.saveBatch(sysUserRoleInsertPos);
    }

    /**
     * 删除系统用户
     *
     * @param userId 用户id
     */
    public void deleteUser(Long userId) {
        sysUserDbService.removeById(userId);
        sysUserRoleDbService.remove(new UpdateWrapper<>(SysUserRolePo.builder()
                .userId(userId)
                .build()));
    }

}
