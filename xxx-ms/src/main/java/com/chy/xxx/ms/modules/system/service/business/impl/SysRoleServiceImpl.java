package com.chy.xxx.ms.modules.system.service.business.impl;

import com.chy.xxx.ms.enums.MsErrorCodeEnum;
import com.chy.xxx.ms.exception.RtBizAssert;
import com.chy.xxx.ms.modules.system.enums.SysResourceTypeEnum;
import com.chy.xxx.ms.modules.system.mapper.SysResourceMapper;
import com.chy.xxx.ms.modules.system.mapper.SysRoleMapper;
import com.chy.xxx.ms.modules.system.mapper.SysUserMapper;
import com.chy.xxx.ms.modules.system.po.*;
import com.chy.xxx.ms.modules.system.qo.*;
import com.chy.xxx.ms.modules.system.service.business.SysRoleService;
import com.chy.xxx.ms.modules.system.service.db.*;
import com.chy.xxx.ms.modules.system.service.tx.SysRoleTxService;
import com.chy.xxx.ms.modules.system.vo.req.*;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRolePageRespVo;
import com.chy.xxx.ms.response.CommonPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * 系统角色业务service实现<br/>
 * 注意：角色配置变更（更新、删除、授权）后不好清除对应的用户资源缓存，可以让用户重新登录，以触发缓存清除拉取最新的、有权限的资源列表
 *
 * @author chy
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleDbService sysRoleDbService;
    @Resource
    private SysUserDbService sysUserDbService;
    @Resource
    private SysResourceDbService sysResourceDbService;
    @Resource
    private SysUserRoleDbService sysUserRoleDbService;
    @Resource
    private SysRoleResourceDbService sysRoleResourceDbService;
    @Resource
    private SysRoleTxService sysRoleTxService;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysResourceMapper sysResourceMapper;

    @Override
    public void add(SysRoleAddReqVo reqVo) {
        String roleName = reqVo.getRoleName();
        List<SysRolePo> sysRolePos = sysRoleDbService.listByQo(SysRoleQo.builder()
                .roleName(roleName)
                .build());
        RtBizAssert.assertEmpty(sysRolePos, MsErrorCodeEnum.ROLE_NAME_ALREADY_EXIST, "roleName=" + roleName);

        SysRolePo sysRolePo = sysRoleMapper.addReqVoToPo(reqVo);
        sysRoleDbService.save(sysRolePo);
    }

    @Override
    public void update(SysRoleUpdateReqVo reqVo) {
        Long id = reqVo.getId();
        SysRolePo sysRolePo = sysRoleDbService.getById(id);
        RtBizAssert.assertNotNull(sysRolePo, MsErrorCodeEnum.SYS_ROLE_NOT_EXIST, "roleId=" + id);

        sysRolePo = sysRoleMapper.updateReqVoToPo(reqVo);
        sysRoleDbService.updateById(sysRolePo);
    }

    @Override
    public void delete(Long id) {
        SysRolePo sysRolePo = sysRoleDbService.getById(id);
        RtBizAssert.assertNotNull(sysRolePo, MsErrorCodeEnum.SYS_ROLE_NOT_EXIST, "roleId=" + id);
        sysRoleTxService.deleteRole(id);
    }

    @Override
    public CommonPage<SysRolePageRespVo> page(SysRolePageReqVo reqVo) {
        //分页查询角色信息
        Page<SysRolePo> page = PageMethod.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        SysRoleQo sysUserQo = sysRoleMapper.pageReqVoToQo(reqVo);
        List<SysRolePo> sysRolePos = sysRoleDbService.listByQo(sysUserQo);
        if (CollectionUtils.isEmpty(sysRolePos)) {
            return CommonPage.empty();
        }
        List<SysRolePageRespVo> respVos = sysRoleMapper.posToPageRespVos(sysRolePos);

        //查询用户-角色关联信息
        List<Long> allRoleIds = sysRolePos.stream()
                .map(SysRolePo::getId)
                .collect(toList());
        List<SysUserRolePo> sysUserRolePos = sysUserRoleDbService.listByQo(SysUserRoleQo.builder()
                .roleIds(allRoleIds)
                .build());
        if (CollectionUtils.isEmpty(sysUserRolePos)) {
            respVos.forEach(respVo -> respVo.setSysUserList(Collections.emptyList()));
            return CommonPage.restPage(page, respVos);
        }
        Map<Long, List<Long>> sysRoleUsersMap = sysUserRolePos.stream()
                .collect(groupingBy(SysUserRolePo::getRoleId, mapping(SysUserRolePo::getUserId, toList())));

        //查询用户信息
        List<Long> allUserIds = sysUserRolePos.stream()
                .map(SysUserRolePo::getUserId)
                .distinct()
                .collect(toList());
        List<SysUserPo> allSysUserPos = sysUserDbService.listByQo(SysUserQo.builder()
                .ids(allUserIds)
                .build());
        Map<Long, SysUserPo> sysUserMap = allSysUserPos.stream()
                .collect(Collectors.toMap(SysUserPo::getId, Function.identity()));

        //填充包含的用户列表
        for (SysRolePageRespVo respVo : respVos) {
            List<Long> userIds = sysRoleUsersMap.getOrDefault(respVo.getId(), Collections.emptyList());
            ArrayList<SysUserPo> sysUserPos = new ArrayList<>(userIds.size());
            userIds.forEach(userId -> sysUserPos.add(sysUserMap.get(userId)));
            respVo.setSysUserList(sysUserMapper.posToRespVos(sysUserPos));
        }
        return CommonPage.restPage(page, respVos);
    }

    @Override
    public List<SysResourceRespVo> listRoleResources(SysRoleResourceListReqVo reqVo) {
        List<SysRoleResourcePo> sysRoleResourcePos = sysRoleResourceDbService.listByQo(SysRoleResourceQo.builder()
                .roleId(reqVo.getRoleId())
                .build());
        if (CollectionUtils.isEmpty(sysRoleResourcePos)) {
            return Collections.emptyList();
        }

        List<Long> resIds = sysRoleResourcePos.stream()
                .map(SysRoleResourcePo::getResId)
                .collect(toList());
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .ids(resIds)
                .resTypes(reqVo.getResTypes())
                .build());
        return sysResourceMapper.posToRespVos(sysResourcePos);
    }

    @Override
    public void saveRoleResources(SysRoleResourceSaveReqVo reqVo) {
        Long roleId = reqVo.getRoleId();
        List<Long> resIds = reqVo.getResIds();

        //添加对应的接口资源
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .parentIds(resIds)
                .resType(SysResourceTypeEnum.INTERFACE.getResType())
                .build());
        List<Long> interfaceResIds = sysResourcePos.stream()
                .map(SysResourcePo::getId)
                .collect(Collectors.toList());
        resIds.addAll(interfaceResIds);

        ArrayList<SysRoleResourcePo> sysRoleResourcePos = new ArrayList<>(resIds.size());
        resIds.forEach(resId -> sysRoleResourcePos.add(SysRoleResourcePo.builder()
                .roleId(roleId)
                .resId(resId)
                .build()));
        sysRoleTxService.grantPermission(roleId, sysRoleResourcePos);
    }

}
