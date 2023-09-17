package com.chy.xxx.ms.modules.system.service.business.impl;

import com.chy.xxx.ms.enums.MsErrorCodeEnum;
import com.chy.xxx.ms.exception.RtBizAssert;
import com.chy.xxx.ms.modules.system.enums.SysResourceTypeEnum;
import com.chy.xxx.ms.modules.system.mapper.SysResourceMapper;
import com.chy.xxx.ms.modules.system.po.SysResourcePo;
import com.chy.xxx.ms.modules.system.qo.SysResourceQo;
import com.chy.xxx.ms.modules.system.service.business.SysResourceService;
import com.chy.xxx.ms.modules.system.service.db.SysResourceDbService;
import com.chy.xxx.ms.modules.system.service.tx.SysResourceTxService;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.security.DynamicSecurityMetadataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
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
    private SysResourceTxService sysResourceTxService;
    @Resource
    private SysResourceMapper sysResourceMapper;
    @Resource
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Override
    public void add(SysResourceAddReqVo reqVo) {
        SysResourcePo sysResourcePo = sysResourceMapper.addReqVoToPo(reqVo);
        //校验资源是否重复
        this.validateResRepeat(sysResourcePo);
        //校验父子资源的resType关联关系
        validateParentResMapping(sysResourcePo.getResType(), sysResourcePo.getParentId());

        sysResourceDbService.save(sysResourcePo);
        dynamicSecurityMetadataSource.reloadAllInterfaceRes();
    }

    @Override
    public void update(SysResourceUpdateReqVo reqVo) {
        Long id = reqVo.getId();
        SysResourcePo sysResourcePo = sysResourceDbService.getById(id);
        RtBizAssert.assertNotNull(sysResourcePo, MsErrorCodeEnum.SYS_RESOURCE_NOT_EXIST, "resId=" + id);

        sysResourcePo = sysResourceMapper.updateReqVoToPo(reqVo);
        //校验资源是否重复
        this.validateResRepeat(sysResourcePo);
        //校验父子资源的resType关联关系
        this.validateParentResMapping(sysResourcePo.getResType(), sysResourcePo.getParentId());

        sysResourceDbService.updateById(sysResourcePo);
        dynamicSecurityMetadataSource.reloadAllInterfaceRes();
    }

    @Override
    public void delete(Long id) {
        SysResourcePo sysResourcePo = sysResourceDbService.getById(id);
        RtBizAssert.assertNotNull(sysResourcePo, MsErrorCodeEnum.SYS_RESOURCE_NOT_EXIST, "resId=" + id);

        //有子资源时不能删除
        List<SysResourcePo> subResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                .parentId(id)
                .build());
        RtBizAssert.assertEmpty(subResourcePos, MsErrorCodeEnum.CANNOT_DELETE_RESOURCE_WITH_SUB_RESOURCES, "resId=" + id);

        sysResourceTxService.deleteResource(id);
        dynamicSecurityMetadataSource.reloadAllInterfaceRes();
    }

    @Override
    public List<SysResourceRespVo> list(SysResourceListReqVo reqVo) {
        SysResourceQo sysResourceQo = sysResourceMapper.listReqVoToQo(reqVo);
        List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(sysResourceQo);
        return sysResourceMapper.posToRespVos(sysResourcePos);
    }

    /**
     * 校验资源是否重复
     *
     * @param sysResourcePo 资源信息
     */
    private void validateResRepeat(SysResourcePo sysResourcePo) {
        SysResourceTypeEnum enumByResType = SysResourceTypeEnum.getEnumByResType(sysResourcePo.getResType());
        Long id = sysResourcePo.getId();
        String resUrl = sysResourcePo.getResUrl();
        String resReqMethod = sysResourcePo.getResReqMethod();

        //菜单、操作/按钮的 resUrl 不能重复
        if (SysResourceTypeEnum.MENU.equals(enumByResType) || SysResourceTypeEnum.OPERATION.equals(enumByResType)) {
            List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                    .resTypes(Arrays.asList(SysResourceTypeEnum.MENU.getResType(), SysResourceTypeEnum.OPERATION.getResType()))
                    .resUrl(resUrl)
                    .build());
            //通过 id==null 判断是新增还是更新
            boolean result = id == null ? CollectionUtils.isEmpty(sysResourcePos) : sysResourcePos.size() <= 1;
            RtBizAssert.assertTrue(result, MsErrorCodeEnum.SYS_MENU_AND_RESOURCE_URL_NOT_REPEAT, "resUrl=" + resUrl);
        }

        //同一父资源下的接口，resReqMethod + resUrl 不能重复
        if (SysResourceTypeEnum.INTERFACE.equals(enumByResType)) {
            List<SysResourcePo> sysResourcePos = sysResourceDbService.listByQo(SysResourceQo.builder()
                    .resTypes(Collections.singletonList(SysResourceTypeEnum.INTERFACE.getResType()))
                    .parentId(sysResourcePo.getParentId())
                    .resReqMethod(resReqMethod)
                    .resUrl(resUrl)
                    .build());
            boolean result = id == null ? CollectionUtils.isEmpty(sysResourcePos) : sysResourcePos.size() <= 1;
            RtBizAssert.assertTrue(result, MsErrorCodeEnum.SYS_INTERFACE_REQ_METHOD_AND_URL_NOT_REPEAT,
                    String.format("resReqMethod=%s，resUrl=%s", resReqMethod, resUrl));
        }
    }

    /**
     * 校验父子资源的resType关联关系
     *
     * @param resType     资源类型
     * @param parentResId 父子源id
     */
    private void validateParentResMapping(Integer resType, Long parentResId) {
        Integer parentResType = null;
        if (parentResId != null && parentResId != 0) {
            SysResourcePo parentResourcePo = sysResourceDbService.getById(parentResId);
            parentResType = parentResourcePo.getResType();
        }
        boolean validateParentResMapping = SysResourceTypeEnum.validateParentResMapping(resType, parentResType);
        RtBizAssert.assertTrue(validateParentResMapping, MsErrorCodeEnum.PARENT_RESOURCE_MAPPING_ERROR,
                String.format("resType=%s,parentResType=%s", resType, parentResType));
    }

}
