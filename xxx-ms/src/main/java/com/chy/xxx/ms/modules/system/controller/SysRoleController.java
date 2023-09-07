package com.chy.xxx.ms.modules.system.controller;

import com.chy.xxx.ms.aop.operationlog.OperationLog;
import com.chy.xxx.ms.modules.system.service.business.SysRoleService;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleGrantPermissionReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRolePageReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysRoleUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysRoleRespVo;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

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
    @OperationLog(value = "分页查询系统角色")
    public CommonResp<CommonPage<SysRoleRespVo>> page(@Valid SysRolePageReqVo reqVo) {
        return sysRoleService.page(reqVo);
    }

    @ApiOperation("系统角色授权")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/roles/permission")
    @OperationLog("系统角色授权")
    public CommonResp<Void> grantPermission(@Valid @RequestBody SysRoleGrantPermissionReqVo reqVo) {
        return sysRoleService.grantPermission(reqVo);
    }

}
