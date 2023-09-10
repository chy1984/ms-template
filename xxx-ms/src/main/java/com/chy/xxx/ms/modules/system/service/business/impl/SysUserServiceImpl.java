package com.chy.xxx.ms.modules.system.service.business.impl;

import com.chy.xxx.ms.component.JwtTokenService;
import com.chy.xxx.ms.enums.ErrorCodeEnum;
import com.chy.xxx.ms.modules.system.bo.SysUserResourceBo;
import com.chy.xxx.ms.modules.system.cache.SysUserResourceCacheService;
import com.chy.xxx.ms.modules.system.enums.SysUserStatusEnum;
import com.chy.xxx.ms.modules.system.mapper.SysResourceMapper;
import com.chy.xxx.ms.modules.system.mapper.SysUserMapper;
import com.chy.xxx.ms.modules.system.po.SysUserPo;
import com.chy.xxx.ms.modules.system.po.SysUserRolePo;
import com.chy.xxx.ms.modules.system.properties.SysUserProperties;
import com.chy.xxx.ms.modules.system.qo.SysUserQo;
import com.chy.xxx.ms.modules.system.qo.SysUserRoleQo;
import com.chy.xxx.ms.modules.system.service.business.SysUserService;
import com.chy.xxx.ms.modules.system.service.db.SysRoleDbService;
import com.chy.xxx.ms.modules.system.service.db.SysUserDbService;
import com.chy.xxx.ms.modules.system.service.db.SysUserRoleDbService;
import com.chy.xxx.ms.modules.system.service.tx.SysUserTxService;
import com.chy.xxx.ms.modules.system.vo.req.SysUserAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserLoginReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserPageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserUpdatePasswordReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysUserUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserDetailRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserLoginRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserPageRespVo;
import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * 系统用户业务service实现
 *
 * @author chy
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserDbService sysUserDbService;
    @Resource
    private SysUserRoleDbService sysUserRoleDbService;
    @Resource
    private SysRoleDbService sysRoleDbService;
    @Resource
    private SysUserTxService sysUserTxService;
    @Resource
    private SysUserResourceCacheService sysUserResourceCacheService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenService jwtTokenService;
    @Resource
    private SysResourceMapper sysResourceMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserProperties sysUserProperties;

    @Override
    public CommonResp<SysUserLoginRespVo> login(SysUserLoginReqVo reqVo) {
        String username = reqVo.getUsername();
        //密码需要客户端加密后传递
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        if (CollectionUtils.isEmpty(sysUserPos)) {
            return CommonResp.fail(ErrorCodeEnum.SYS_USER_NOT_EXIST, null);
        }
        SysUserPo sysUserPo = sysUserPos.get(0);
        if (!passwordEncoder.matches(reqVo.getPassword(), sysUserPo.getPassword())) {
            return CommonResp.fail(ErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR, null);
        }
        if (SysUserStatusEnum.DISABLED.getStatus().equals(sysUserPo.getStatus())) {
            return CommonResp.fail(ErrorCodeEnum.SYS_USER_DISABLED, null);
        }

        //请求上下文回填用户信息
        RequestContextHolder.setUserInfo(sysUserPo.getUsername(), sysUserPo.getRealName());

        String token = jwtTokenService.generateToken(sysUserPo);
        SysUserLoginRespVo respVo = SysUserLoginRespVo.builder()
                .token(token)
                .build();
        return CommonResp.success(respVo);
    }

    @Override
    public CommonResp<Void> logout(String username) {
        //更新角色、资源时没清除用户资源缓存，把登出作为缓存清除触发点，更新资源、角色配置后，可以让用户重新登录以拉取最新的、有权限的资源列表
        sysUserResourceCacheService.removeUserResourceCache(username);
        return CommonResp.success();
    }

    @Override
    public CommonResp<SysUserDetailRespVo> getUserDetail(String username) {
        SysUserResourceBo sysUserResourceBo = sysUserResourceCacheService.getUseResourceCache(username);
        List<SysResourceRespVo> menuList = sysResourceMapper.posToRespVos(sysUserResourceBo.getMenuList());
        List<SysResourceRespVo> operationList = sysResourceMapper.posToRespVos(sysUserResourceBo.getOperationList());
        SysUserDetailRespVo respVo = SysUserDetailRespVo.builder()
                .username(sysUserResourceBo.getUsername())
                .realName(sysUserResourceBo.getRealName())
                .menuList(menuList)
                .operationList(operationList)
                .build();
        return CommonResp.success(respVo);
    }

    @Override
    public CommonResp<Void> add(SysUserAddReqVo reqVo) {
        String username = reqVo.getUsername();
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        if (CollectionUtils.isNotEmpty(sysUserPos)) {
            return CommonResp.fail(ErrorCodeEnum.USERNAME_ALREADY_EXIST);
        }

        SysUserPo sysUserPo = sysUserMapper.addReqVoToPo(reqVo);
        String defaultPassword = passwordEncoder.encode(sysUserProperties.getDefaultPassword());
        sysUserPo.setPassword(defaultPassword);
        ArrayList<SysUserRolePo> sysUserRolePos = new ArrayList<>();
        reqVo.getRoleIds().forEach(roleId -> sysUserRolePos.add(SysUserRolePo.builder()
                .roleId(roleId)
                .build()));

        sysUserTxService.addUser(sysUserPo, sysUserRolePos);
        sysUserResourceCacheService.removeUserResourceCache(username);
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> update(SysUserUpdateReqVo reqVo) {
        Long userId = reqVo.getId();
        SysUserPo sysUserPo = sysUserDbService.getById(userId);
        if (sysUserPo == null) {
            return CommonResp.fail(ErrorCodeEnum.SYS_USER_NOT_EXIST);
        }

        SysUserPo sysUserUpdatePo = sysUserMapper.updateReqVoToPo(reqVo);
        ArrayList<SysUserRolePo> sysUserRoleInsertPos = new ArrayList<>();
        reqVo.getRoleIds().forEach(roleId -> sysUserRoleInsertPos.add(SysUserRolePo.builder()
                .userId(userId)
                .roleId(roleId)
                .build()));

        sysUserTxService.updateUser(sysUserUpdatePo, sysUserRoleInsertPos);
        sysUserResourceCacheService.removeUserResourceCache(sysUserPo.getUsername());
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> delete(Long id) {
        SysUserPo sysUserPo = sysUserDbService.getById(id);
        if (sysUserPo == null) {
            return CommonResp.fail(ErrorCodeEnum.SYS_USER_NOT_EXIST);
        }
        sysUserTxService.deleteUser(id);
        return CommonResp.success();
    }

    @Override
    public CommonResp<CommonPage<SysUserPageRespVo>> page(SysUserPageReqVo reqVo) {
        //通过roleId置换出userId
        List<Long> userIds = Collections.emptyList();
        if (reqVo.getRoleId() != null) {
            List<SysUserRolePo> sysUserRolePos = sysUserRoleDbService.listByQo(SysUserRoleQo.builder()
                    .roleId(reqVo.getRoleId())
                    .build());
            if (CollectionUtils.isEmpty(sysUserRolePos)) {
                return CommonResp.success(CommonPage.empty());
            }
            userIds = sysUserRolePos.stream()
                    .map(SysUserRolePo::getUserId)
                    .collect(toList());
        }

        //分页查询用户信息
        Page<SysUserPo> page = PageMethod.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        SysUserQo sysUserQo = sysUserMapper.pageReqVoToQo(reqVo);
        sysUserQo.setIds(userIds);
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(sysUserQo);
        if (CollectionUtils.isEmpty(sysUserPos)) {
            return CommonResp.success(CommonPage.empty());
        }

        //查询用户-角色关联信息
        userIds = sysUserPos.stream()
                .map(SysUserPo::getId)
                .collect(toList());
        List<SysUserRolePo> sysUserRolePos = sysUserRoleDbService.listByQo(SysUserRoleQo.builder()
                .userIds(userIds)
                .build());
        Map<Long, List<Long>> sysUserRolesMap = sysUserRolePos.stream()
                .collect(groupingBy(SysUserRolePo::getUserId, mapping(SysUserRolePo::getRoleId, toList())));

        //填充角色id列表
        List<SysUserPageRespVo> respVos = sysUserMapper.posToPageRespVos(sysUserPos);
        respVos.forEach(respVo -> respVo.setRoleIds(sysUserRolesMap.get(respVo.getId())));

        return CommonResp.success(CommonPage.restPage(page, respVos));
    }

    @Override
    public CommonResp<Void> resetPassword(Long id) {
        SysUserPo sysUserPo = sysUserDbService.getById(id);
        if (sysUserPo == null) {
            return CommonResp.fail(ErrorCodeEnum.SYS_USER_NOT_EXIST);
        }

        String newPassword = passwordEncoder.encode(sysUserProperties.getDefaultPassword());
        sysUserDbService.updateById(SysUserPo.builder()
                .id(id)
                .password(newPassword)
                .build());
        sysUserResourceCacheService.removeUserResourceCache(sysUserPo.getUsername());
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> updatePassword(SysUserUpdatePasswordReqVo reqVo) {
        String username = RequestContextHolder.getRequestContext().getUsername();
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        SysUserPo sysUserPo = sysUserPos.get(0);
        if (!passwordEncoder.matches(reqVo.getOldPassword(), sysUserPo.getPassword())) {
            return CommonResp.fail(ErrorCodeEnum.OLD_PASSWORD_ERROR);
        }

        String newPassword = passwordEncoder.encode(reqVo.getNewPassword());
        sysUserDbService.updateById(SysUserPo.builder()
                .id(sysUserPo.getId())
                .password(newPassword)
                .build());
        sysUserResourceCacheService.removeUserResourceCache(username);
        return CommonResp.success();
    }

}
