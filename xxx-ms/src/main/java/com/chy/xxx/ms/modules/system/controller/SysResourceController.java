package com.chy.xxx.ms.modules.system.controller;

import com.chy.xxx.ms.aop.operationlog.OperationLog;
import com.chy.xxx.ms.modules.system.service.business.SysResourceService;
import com.chy.xxx.ms.modules.system.vo.req.SysResourceAddReqVo;
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
        return sysResourceService.add(reqVo);
    }

    @ApiOperation("更新系统资源")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @PutMapping("/v1/system/resources")
    @OperationLog("更新系统资源")
    public CommonResp<Void> update(@Valid @RequestBody SysResourceUpdateReqVo reqVo) {
        return sysResourceService.update(reqVo);
    }

    @ApiOperation("删除系统资源")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @DeleteMapping("/v1/system/resources/{id}")
    @OperationLog("删除系统资源")
    public CommonResp<Void> delete(@PathVariable("id") Long id) {
        return sysResourceService.delete(id);
    }

    @ApiOperation("查询全部系统资源")
    @ApiOperationSupport(author = "chy chy@qq.com")
    @GetMapping("/v1/system/resources/all")
    @OperationLog(value = "查询全部系统资源", saveRespData = false)
    public CommonResp<List<SysResourceRespVo>> getAll() {
        return sysResourceService.getAll();
    }

}
