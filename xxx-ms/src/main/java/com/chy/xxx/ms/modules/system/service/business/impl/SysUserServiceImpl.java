package com.chy.xxx.ms.modules.system.service.business.impl;

import com.chy.xxx.ms.component.JwtTokenService;
import com.chy.xxx.ms.enums.MsErrorCodeEnum;
import com.chy.xxx.ms.exception.RtBizAssert;
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
import com.chy.xxx.ms.modules.system.service.db.SysUserDbService;
import com.chy.xxx.ms.modules.system.service.db.SysUserRoleDbService;
import com.chy.xxx.ms.modules.system.service.tx.SysUserTxService;
import com.chy.xxx.ms.modules.system.vo.req.*;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserDetailRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserPageRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserTokenRespVo;
import com.chy.xxx.ms.properties.JwtTokenProperties;
import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.response.CommonPage;
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

import static java.util.stream.Collectors.*;

/**
 * 系统用户业务service实现<br/>
 * 说明：管理后台一般部署在内网、仅限内网ip访问，相对安全，此处的密码并未加密传输
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
    private SysUserTxService sysUserTxService;
    @Resource
    private SysUserResourceCacheService sysUserResourceCacheService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private JwtTokenService jwtTokenService;
    @Resource
    private JwtTokenProperties jwtTokenProperties;
    @Resource
    private SysResourceMapper sysResourceMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysUserProperties sysUserProperties;

    @Override
    public SysUserTokenRespVo login(SysUserLoginReqVo reqVo) {
        String username = reqVo.getUsername();
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        RtBizAssert.assertNotEmpty(sysUserPos, MsErrorCodeEnum.SYS_USER_NOT_EXIST, "username=" + username);

        SysUserPo sysUserPo = sysUserPos.get(0);
        RtBizAssert.assertTrue(passwordEncoder.matches(reqVo.getPassword(), sysUserPo.getPassword()),
                MsErrorCodeEnum.USERNAME_OR_PASSWORD_ERROR, "username=" + username);

        RtBizAssert.assertFalse(SysUserStatusEnum.DISABLED.getStatus().equals(sysUserPo.getStatus()),
                MsErrorCodeEnum.SYS_USER_DISABLED, "username=" + username);

        //请求上下文回填用户信息
        RequestContextHolder.setUserInfo(sysUserPo.getUsername(), sysUserPo.getRealName());

        String token = jwtTokenService.generateToken(sysUserPo);
        return SysUserTokenRespVo.builder()
                .token(token)
                .tokenPrefix(jwtTokenProperties.getPrefix())
                .build();
    }

    @Override
    public void logout(String username) {
        //更新角色、资源时没清除用户资源缓存，把登出作为缓存清除触发点，更新资源、角色配置后，可以让用户重新登录以拉取最新的、有权限的资源列表
        sysUserResourceCacheService.removeUserResourceCache(username);
    }

    @Override
    public SysUserDetailRespVo getUserDetail(String username) {
        SysUserResourceBo sysUserResourceBo = sysUserResourceCacheService.getUseResourceCache(username);
        List<SysResourceRespVo> menuList = sysResourceMapper.posToRespVos(sysUserResourceBo.getMenuList());
        List<SysResourceRespVo> operationList = sysResourceMapper.posToRespVos(sysUserResourceBo.getOperationList());
        return SysUserDetailRespVo.builder()
                .username(sysUserResourceBo.getUsername())
                .realName(sysUserResourceBo.getRealName())
                .menuList(menuList)
                .operationList(operationList)
                .build();
    }

    @Override
    public SysUserTokenRespVo refreshToken(String token) {
        token = jwtTokenService.refreshToken(token);
        return SysUserTokenRespVo.builder()
                .token(token)
                .tokenPrefix(jwtTokenProperties.getPrefix())
                .build();
    }

    @Override
    public void add(SysUserAddReqVo reqVo) {
        String username = reqVo.getUsername();
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        RtBizAssert.assertEmpty(sysUserPos, MsErrorCodeEnum.USERNAME_ALREADY_EXIST, "username=" + username);

        SysUserPo sysUserPo = sysUserMapper.addReqVoToPo(reqVo);
        String defaultPassword = passwordEncoder.encode(sysUserProperties.getDefaultPassword());
        sysUserPo.setPassword(defaultPassword);
        ArrayList<SysUserRolePo> sysUserRolePos = new ArrayList<>();
        reqVo.getRoleIds().forEach(roleId -> sysUserRolePos.add(SysUserRolePo.builder()
                .roleId(roleId)
                .build()));

        sysUserTxService.addUser(sysUserPo, sysUserRolePos);
        sysUserResourceCacheService.removeUserResourceCache(username);
    }

    @Override
    public void update(SysUserUpdateReqVo reqVo) {
        Long userId = reqVo.getId();
        SysUserPo sysUserPo = sysUserDbService.getById(userId);
        RtBizAssert.assertNotNull(sysUserPo, MsErrorCodeEnum.SYS_USER_NOT_EXIST, "userId=" + userId);

        SysUserPo sysUserUpdatePo = sysUserMapper.updateReqVoToPo(reqVo);
        ArrayList<SysUserRolePo> sysUserRoleInsertPos = new ArrayList<>();
        reqVo.getRoleIds().forEach(roleId -> sysUserRoleInsertPos.add(SysUserRolePo.builder()
                .userId(userId)
                .roleId(roleId)
                .build()));

        sysUserTxService.updateUser(sysUserUpdatePo, sysUserRoleInsertPos);
        sysUserResourceCacheService.removeUserResourceCache(sysUserPo.getUsername());
    }

    @Override
    public void delete(Long id) {
        SysUserPo sysUserPo = sysUserDbService.getById(id);
        RtBizAssert.assertNotNull(sysUserPo, MsErrorCodeEnum.SYS_USER_NOT_EXIST, "userId=" + id);
        String curUsername = RequestContextHolder.getRequestContext().getUsername();
        RtBizAssert.assertNotEquals(sysUserPo.getUsername(), curUsername, MsErrorCodeEnum.CANNOT_DELETE_SELF, "username=" + curUsername);

        sysUserTxService.deleteUser(id);
        sysUserResourceCacheService.removeUserResourceCache(sysUserPo.getUsername());
    }

    @Override
    public CommonPage<SysUserPageRespVo> page(SysUserPageReqVo reqVo) {
        //通过roleId置换出userId
        List<Long> userIds = Collections.emptyList();
        if (reqVo.getRoleId() != null) {
            List<SysUserRolePo> sysUserRolePos = sysUserRoleDbService.listByQo(SysUserRoleQo.builder()
                    .roleId(reqVo.getRoleId())
                    .build());
            if (CollectionUtils.isEmpty(sysUserRolePos)) {
                return CommonPage.empty();
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
            return CommonPage.empty();
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

        return CommonPage.restPage(page, respVos);
    }

    @Override
    public void resetPassword(Long id) {
        SysUserPo sysUserPo = sysUserDbService.getById(id);
        RtBizAssert.assertNotNull(sysUserPo, MsErrorCodeEnum.SYS_USER_NOT_EXIST, "userId=" + id);

        String newPassword = passwordEncoder.encode(sysUserProperties.getDefaultPassword());
        sysUserDbService.updateById(SysUserPo.builder()
                .id(id)
                .password(newPassword)
                .build());
        sysUserResourceCacheService.removeUserResourceCache(sysUserPo.getUsername());
    }

    @Override
    public void updatePassword(SysUserUpdatePasswordReqVo reqVo) {
        String username = RequestContextHolder.getRequestContext().getUsername();
        List<SysUserPo> sysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .username(username)
                .build());
        SysUserPo sysUserPo = sysUserPos.get(0);
        RtBizAssert.assertTrue(passwordEncoder.matches(reqVo.getOldPassword(), sysUserPo.getPassword()),
                MsErrorCodeEnum.OLD_PASSWORD_ERROR, "username=" + username);

        String newPassword = passwordEncoder.encode(reqVo.getNewPassword());
        sysUserDbService.updateById(SysUserPo.builder()
                .id(sysUserPo.getId())
                .password(newPassword)
                .build());
        sysUserResourceCacheService.removeUserResourceCache(username);
    }

}
