package com.eyxyt.basement.controller;

import com.eyxyt.basement.annotation.SysLog;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.bean.QueryParams;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 系统日志
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 17:51:12
 */
@Api(tags = "系统日志 管理")
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @RequiresPermissions("sys:log:list")
    public Result<PageVo> list(QueryParams params){
        PageVo page = sysLogService.queryPage(new Query(params));

        return Result.ok(page);
    }

    /**
     * 删除
     */
    @SysLog("删除系统日志")
    @ApiOperation("删除")
    @PostMapping("/delete")
    @RequiresPermissions("sys:log:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
        sysLogService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }
}
