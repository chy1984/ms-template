package com.chy.xxx.ms.modules.system.cache;

import com.chy.xxx.ms.aop.cache.Cacheable;
import com.chy.xxx.ms.aop.cache.Param;
import com.chy.xxx.ms.aop.cache.RemoveCache;
import com.chy.xxx.ms.constant.CacheExpirationConstant;
import com.chy.xxx.ms.constant.CacheNameConstant;
import com.chy.xxx.ms.modules.system.bo.SysUserResourceBo;
import com.chy.xxx.ms.modules.system.enums.SysResourceStatusEnum;
import com.chy.xxx.ms.modules.system.enums.SysRoleStatusEnum;
import com.chy.xxx.ms.modules.system.enums.SysUserStatusEnum;
import com.chy.xxx.ms.modules.system.po.*;
import com.chy.xxx.ms.modules.system.qo.*;
import com.chy.xxx.ms.modules.system.service.db.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统用户资源信息缓存服务
 *
 * @author chy
 */
@Slf4j
@Service
public class SysUserResourceCacheService {

    @Resource
    private SysUserDbService sysUserDbService;
    @Resource
    private SysRoleDbService sysRoleDbService;
    @Resource
    private SysResourceDbService sysResourceDbService;
    @Resource
    private SysUserRoleDbService sysUserRoleDbService;
    @Resource
    private SysRoleResourceDbService sysRoleResourceDbService;

    @Cacheable(cacheName = CacheNameConstant.SYSTEM_USER_RESOURCE_INFO, cacheParams = "username", expiration = CacheExpirationConstant.HOURS_1)
    public SysUserResourceBo getUseResourceCache(@Param("username") String username) {
        //查询用户信息
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        if (CollectionUtils.isEmpty(sysUserPos)) {
            log.warn("用户不存在,username={}", username);
            return SysUserResourceBo.buildUserNotExist(username);
        }

        SysUserPo sysUserPo = sysUserPos.get(0);
        if (SysUserStatusEnum.DISABLED.getStatus().equals(sysUserPo.getStatus())) {
            log.warn("用户账号已被禁用,username={}", username);
            return SysUserResourceBo.buildUserNoResources(sysUserPo);
        }

        //查询用户-角色关联关系
        List<SysUserRolePo> sysUserRolePos = sysUserRoleDbService.listByQo(SysUserRoleQo.builder()
                .userId(sysUserPo.getId())
                .build());
        if (CollectionUtils.isEmpty(sysUserRolePos)) {
            log.warn("用户未关联角色,username={}", username);
            return SysUserResourceBo.buildUserNoResources(sysUserPo);
        }

        //查询拥有的、有效的角色
        List<Long> roleIds = sysUserRolePos.stream()
                .map(SysUserRolePo::getRoleId)
                .collect(Collectors.toList());
        List<SysRolePo> sysRolePos = sysRoleDbService.listByQo(SysRoleQo.builder()
                .ids(roleIds)
                .status(SysRoleStatusEnum.NORMAL.getStatus())
                .build());
        roleIds = sysRolePos.stream()
                .map(SysRolePo::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(roleIds)) {
            log.warn("用户没有有效的角色,username={}", username);
            return SysUserResourceBo.buildUserNoResources(sysUserPo);
        }

        //查询角色-资源关联关系
        List<SysRoleResourcePo> sysRoleResourcePos = sysRoleResourceDbService.listByQo(SysRoleResourceQo.builder()
                .roleIds(roleIds)
                .build());
        if (CollectionUtils.isEmpty(sysRoleResourcePos)) {
            log.warn("用户拥有的角色均未关联资源,username={},roleIds={}", username, roleIds);
            return SysUserResourceBo.buildUserNoResources(sysUserPo);
        }

        //查询拥有的、有效的资源
        List<Long> resIds = sysRoleResourcePos.stream()
                .map(SysRoleResourcePo::getResId)
                .distinct()
                .collect(Collectors.toList());
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .ids(resIds)
                .status(SysResourceStatusEnum.NORMAL.getStatus())
                .build());
        if (CollectionUtils.isEmpty(sysResourcePos)) {
            log.info("用户拥有的资源均无效,username={},resIds={}", username, resIds);
        }

        //处理结果
        Map<Integer, List<SysResourcePo>> resTypeMap = sysResourcePos.stream()
                .collect(Collectors.groupingBy(SysResourcePo::getResType));
        return SysUserResourceBo.buildUserWithResources(sysUserPo, resTypeMap);
    }

    @RemoveCache(cacheName = CacheNameConstant.SYSTEM_USER_RESOURCE_INFO, cacheParams = "username")
    public void removeUserResourceCache(@Param("username") String username) {
        log.info("清除系统用户资源信息缓存,username={}", username);
    }

}
