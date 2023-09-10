package com.chy.xxx.ms.modules.system.controller;

import com.chy.xxx.ms.aop.operationlog.OperationLog;
import com.chy.xxx.ms.modules.system.service.business.SysUserService;
import com.chy.xxx.ms.modules.system.vo.req.*;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserDetailRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserLoginRespVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysUserPageRespVo;
import com.chy.xxx.ms.request.RequestContextHolder;
import com.chy.xxx.ms.response.CommonPage;
import com.chy.xxx.ms.response.CommonResp;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 系统用户Controller
 *
 * @author chy
 */
@RestController
@Api(tags = "系统用户")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @ApiOperation("添加系统用户")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PostMapping("/v1/system/users")
    @OperationLog("添加系统用户")
    public CommonResp<Void> add(@Valid @RequestBody SysUserAddReqVo reqVo) {
        return sysUserService.add(reqVo);
    }

    @ApiOperation("更新系统用户信息")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/users")
    @OperationLog("更新系统用户信息")
    public CommonResp<Void> update(@Valid @RequestBody SysUserUpdateReqVo reqVo) {
        return sysUserService.update(reqVo);
    }

    @ApiOperation("删除系统用户")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @DeleteMapping("/v1/system/users/{id}")
    @OperationLog("删除系统用户")
    public CommonResp<Void> delete(@PathVariable("id") Long id) {
        return sysUserService.delete(id);
    }

    @ApiOperation("分页查询系统用户")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @GetMapping("/v1/system/users/page")
    @OperationLog("分页查询系统用户")
    public CommonResp<CommonPage<SysUserPageRespVo>> page(@Valid SysUserPageReqVo reqVo) {
        return sysUserService.page(reqVo);
    }

    @ApiOperation("登录")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PostMapping("/v1/system/users/login")
    @OperationLog("登录")
    public CommonResp<SysUserLoginRespVo> login(@Valid @RequestBody SysUserLoginReqVo reqVo) {
        return sysUserService.login(reqVo);
    }

    @ApiOperation("登出")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/users/logout")
    @OperationLog("登出")
    public CommonResp<Void> logout() {
        String username = RequestContextHolder.getRequestContext().getUsername();
        return sysUserService.logout(username);
    }

    @ApiOperation("查询当前用户详细信息")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @GetMapping("/v1/system/users/detail")
    @OperationLog(value = "查询当前用户详细信息", saveRespData = false)
    public CommonResp<SysUserDetailRespVo> detail() {
        String username = RequestContextHolder.getRequestContext().getUsername();
        return sysUserService.getUserDetail(username);
    }

    @ApiOperation("修改当前用户密码")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/users/password")
    @OperationLog("修改当前用户密码")
    public CommonResp<Void> updatePassword(@Valid @RequestBody SysUserUpdatePasswordReqVo reqVo) {
        return sysUserService.updatePassword(reqVo);
    }

    @ApiOperation("重置系统用户密码")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/users/{id}/password/reset")
    @OperationLog("重置系统用户密码")
    public CommonResp<Void> resetPassword(@PathVariable("id") Long id) {
        return sysUserService.resetPassword(id);
    }

}
