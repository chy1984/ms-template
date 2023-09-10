package com.chy.xxx.ms.modules.system.controller;

import com.chy.xxx.ms.aop.operationlog.OperationLog;
import com.chy.xxx.ms.modules.system.service.business.SysRoleService;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRolePageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleResourceSaveReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRolePageRespVo;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 系统角色Controller
 *
 * @author chy
 */
@RestController
@Api(tags = "系统角色")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @ApiOperation("添加系统角色")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PostMapping("/v1/system/roles")
    @OperationLog("添加系统角色")
    public CommonResp<Void> add(@Valid @RequestBody SysRoleAddReqVo reqVo) {
        return sysRoleService.add(reqVo);
    }

    @ApiOperation("更新系统角色")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/roles")
    @OperationLog("更新系统角色")
    public CommonResp<Void> update(@Valid @RequestBody SysRoleUpdateReqVo reqVo) {
        return sysRoleService.update(reqVo);
    }

    @ApiOperation("删除系统角色")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @DeleteMapping("/v1/system/roles/{id}")
    @OperationLog("删除系统角色")
    public CommonResp<Void> delete(@PathVariable("id") Long id) {
        return sysRoleService.delete(id);
    }

    @ApiOperation("分页查询系统角色")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @GetMapping("/v1/system/roles/page")
    @OperationLog("分页查询系统角色")
    public CommonResp<CommonPage<SysRolePageRespVo>> page(@Valid SysRolePageReqVo reqVo) {
        return sysRoleService.page(reqVo);
    }

    @ApiOperation("查询系统角色具有的资源列表")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @GetMapping("/v1/system/roles/resources/list")
    @OperationLog("查询系统角色具有的资源列表")
    public CommonResp<List<SysResourceRespVo>> listRoleResources(@Valid SysRoleResourceListReqVo reqVo) {
        return sysRoleService.listRoleResources(reqVo);
    }

    @ApiOperation("保存系统角色资源配置")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PostMapping("/v1/system/roles/resources")
    @OperationLog("保存系统角色资源配置")
    public CommonResp<Void> saveRoleResources(@Valid @RequestBody SysRoleResourceSaveReqVo reqVo) {
        return sysRoleService.saveRoleResources(reqVo);
    }

}
