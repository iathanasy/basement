package com.eyxyt.basement.controller;

import java.util.Arrays;
import com.eyxyt.basement.annotation.SysLog;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.bean.QueryParams;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.entity.SysConfigEntity;
import com.eyxyt.basement.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 系统配置信息表
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@Api(tags = "系统配置信息表 管理")
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 所有配置列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @RequiresPermissions("sys:config:list")
    public Result<PageVo> list(QueryParams params){
        PageVo page = sysConfigService.queryPage(new Query(params));

        return Result.ok(page);
    }


    /**
     * 配置信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:config:info")
    public Result<PageVo> info(@PathVariable("id") Long id){
        SysConfigEntity config = sysConfigService.getById(id);

        return Result.ok(config);
    }

    /**
     * 保存配置
     */

    @SysLog("保存配置")
    @ApiOperation("保存配置")
    @PostMapping("/save")
    @RequiresPermissions("sys:config:save")
    public Result save(@RequestBody SysConfigEntity config){
        sysConfigService.save(config);

        return Result.ok();
    }

    /**
     * 修改配置
     */
    @SysLog("修改配置")
    @ApiOperation("修改配置")
    @PostMapping("/update")
    @RequiresPermissions("sys:config:update")
    public Result update(@RequestBody SysConfigEntity config){

        sysConfigService.updateById(config);
        return Result.ok();
    }

    /**
     * 删除配置
     */
    @SysLog("删除配置")
    @ApiOperation("删除配置")
    @PostMapping("/delete")
    @RequiresPermissions("sys:config:delete")
    public Result delete(@RequestBody Long[] ids){
        sysConfigService.removeByIds(Arrays.asList(ids));
        return Result.ok();
    }
}
