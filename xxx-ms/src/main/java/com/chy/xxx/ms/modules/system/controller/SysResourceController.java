package com.chy.xxx.ms.modules.system.controller;

import com.chy.xxx.ms.aop.operationlog.OperationLog;
import com.chy.xxx.ms.modules.system.service.business.SysResourceService;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceListReqVo;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceUpdateReqVo;
import com.chy.xxx.ms.modules.system.vo.resp.SysResourceRespVo;
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
 * 系统资源Controller
 *
 * @author chy
 */
@RestController
@Api(tags = "系统资源")
public class SysResourceController {

    @Resource
    private SysResourceService sysResourceService;

    @ApiOperation("添加系统资源")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PostMapping("/v1/system/resources")
    @OperationLog("添加系统资源")
    public CommonResp<Void> add(@Valid @RequestBody SysResourceAddReqVo reqVo) {
        sysResourceService.add(reqVo);
        return CommonResp.success();
    }

    @ApiOperation("更新系统资源")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/resources")
    @OperationLog("更新系统资源")
    public CommonResp<Void> update(@Valid @RequestBody SysResourceUpdateReqVo reqVo) {
        sysResourceService.update(reqVo);
        return CommonResp.success();
    }

    @ApiOperation("删除系统资源")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @DeleteMapping("/v1/system/resources/{id}")
    @OperationLog("删除系统资源")
    public CommonResp<Void> delete(@PathVariable("id") Long id) {
        sysResourceService.delete(id);
        return CommonResp.success();
    }

    @ApiOperation("查询系统资源列表")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @GetMapping("/v1/system/resources/list")
    @OperationLog(value = "查询系统资源列表", saveRespData = false)
    public CommonResp<List<SysResourceRespVo>> list(@Valid SysResourceListReqVo reqVo) {
        return CommonResp.success(sysResourceService.list(reqVo));
    }

}
