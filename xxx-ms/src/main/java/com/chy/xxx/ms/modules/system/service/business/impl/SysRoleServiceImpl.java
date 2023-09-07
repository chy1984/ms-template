package com.chy.xxx.ms.modules.system.service.business.impl;

import com.chy.xxx.ms.enums.ErrorCodeEnum;
import com.chy.xxx.ms.modules.system.mapper.SysRoleMapper;
import com.chy.xxx.ms.modules.system.po.SysRolePo;
import com.chy.xxx.ms.modules.system.po.SysRoleResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysRoleQo;
import com.chy.xxx.ms.modules.system.service.business.SysRoleService;
import com.chy.xxx.ms.modules.system.service.db.SysRoleDbService;
import com.chy.xxx.ms.modules.system.service.tx.SysRoleTxService;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleGrantPermissionReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRolePageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRoleRespVo;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private SysRoleTxService sysRoleTxService;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public CommonResp<Void> add(SysRoleAddReqVo reqVo) {
        List<SysRolePo> sysRolePos = sysRoleDbService.listByQo(SysRoleQo.builder()
                .roleName(reqVo.getRoleName())
                .build());
        if (CollectionUtils.isNotEmpty(sysRolePos)) {
            return CommonResp.fail(ErrorCodeEnum.ROLE_NAME_ALREADY_EXIST);
        }

        SysRolePo sysRolePo = sysRoleMapper.addReqVoToPo(reqVo);
        sysRoleDbService.save(sysRolePo);
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> update(SysRoleUpdateReqVo reqVo) {
        SysRolePo sysRolePo = sysRoleDbService.getById(reqVo.getId());
        if (sysRolePo == null) {
            CommonResp.fail(ErrorCodeEnum.SYS_ROLE_NOT_EXIST);
        }

        sysRolePo = sysRoleMapper.updateReqVoToPo(reqVo);
        sysRoleDbService.updateById(sysRolePo);
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> delete(Long id) {
        SysRolePo sysRolePo = sysRoleDbService.getById(id);
        if (sysRolePo == null) {
            return CommonResp.fail(ErrorCodeEnum.SYS_ROLE_NOT_EXIST);
        }
        sysRoleDbService.removeById(id);
        return CommonResp.success();
    }

    @Override
    public CommonResp<CommonPage<SysRoleRespVo>> page(SysRolePageReqVo reqVo) {
        Page<SysRolePo> poPage = PageMethod.startPage(reqVo.getPageNum(), reqVo.getPageSize());
        SysRoleQo sysUserQo = sysRoleMapper.pageReqVoToQo(reqVo);
        List<SysRolePo> sysRolePos = sysRoleDbService.listByQo(sysUserQo);
        if (CollectionUtils.isEmpty(sysRolePos)) {
            return CommonResp.success(CommonPage.empty());
        }

        Page<SysRoleRespVo> respVoPage = sysRoleMapper.poPageToRespVoPage(poPage);
        CommonPage<SysRoleRespVo> commonPage = CommonPage.restPage(respVoPage);
        return CommonResp.success(commonPage);
    }

    @Override
    public CommonResp<Void> grantPermission(SysRoleGrantPermissionReqVo reqVo) {
        Long roleId = reqVo.getRoleId();
        List<Long> resIds = reqVo.getResIds();
        ArrayList<SysRoleResourcePo> sysRoleResourcePos = new ArrayList<>(resIds.size());
        for (Long resId : resIds) {
            sysRoleResourcePos.add(SysRoleResourcePo.builder()
                    .roleId(roleId)
                    .resId(resId)
                    .build());
        }
        sysRoleTxService.grantPermission(roleId, sysRoleResourcePos);
        return CommonResp.success();
    }

}
