package com.chy.xxx.ms.modules.system.service.business.impl;

import com.chy.xxx.ms.enums.ErrorCodeEnum;
import com.chy.xxx.ms.modules.system.mapper.SysResourceMapper;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;
import com.chy.xxx.ms.modules.system.service.business.SysResourceService;
import com.chy.xxx.ms.modules.system.service.db.SysResourceDbService;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.response.CommonResp;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统资源业务service实现<br/>
 * 注意：资源配置变更（更新、删除）后不好清除对应的用户资源缓存，可以让用户重新登录，以触发缓存清除拉取最新的、有权限的资源列表
 *
 * @author chy
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    @Resource
    private SysResourceDbService sysResourceDbService;
    @Resource
    private SysResourceMapper sysResourceMapper;

    @Override
    public CommonResp<Void> add(SysResourceAddReqVo reqVo) {
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .resReqMethod(reqVo.getResReqMethod())
                .resUrl(reqVo.getResUrl())
                .build());
        if (CollectionUtils.isNotEmpty(sysResourcePos)) {
            return CommonResp.fail(ErrorCodeEnum.RESOURCE_REQ_METHOD_AND_URL_ALREADY_EXIST);
        }

        SysResourcePo sysResourcePo = sysResourceMapper.addReqVoToPo(reqVo);
        sysResourceDbService.save(sysResourcePo);
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> update(SysResourceUpdateReqVo reqVo) {
        SysResourcePo sysResourcePo = sysResourceDbService.getById(reqVo.getId());
        if (sysResourcePo == null) {
            return CommonResp.fail(ErrorCodeEnum.SYS_RESOURCE_NOT_EXIST);
        }

        sysResourcePo = sysResourceMapper.updateReqVoToPo(reqVo);
        sysResourceDbService.updateById(sysResourcePo);
        return CommonResp.success();
    }

    @Override
    public CommonResp<Void> delete(Long id) {
        sysResourceDbService.removeById(id);
        return CommonResp.success();
    }

    @Override
    public CommonResp<List<SysResourceRespVo>> getAll() {
        List<SysResourcePo> sysResourcePos = sysResourceDbService.list();
        List<SysResourceRespVo> resourceRespVos = sysResourceMapper.posToRespVos(sysResourcePos);
        return CommonResp.success(resourceRespVos);
    }

}
