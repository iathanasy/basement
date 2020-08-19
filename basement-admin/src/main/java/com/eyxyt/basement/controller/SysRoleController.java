package com.eyxyt.basement.controller;

import com.eyxyt.basement.annotation.SysLog;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.bean.QueryParams;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.constant.Constant;
import com.eyxyt.basement.entity.SysRoleEntity;
import com.eyxyt.basement.service.SysRoleMenuService;
import com.eyxyt.basement.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




/**
 * 角色
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 17:51:12
 */
@Api(tags = "角色 管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController{

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @RequiresPermissions("sys:role:list")
    public Result<PageVo> list(QueryParams params) {
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(!Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(getUserName())){
            params.setCreateBy(getUserName());
        }
        PageVo page = sysRoleService.queryPage(new Query(params));
        return Result.ok(page);
    }

    /**
     * 角色选择
     * @return
     */
    @ApiOperation("角色选择")
    @GetMapping("/select")
    @RequiresPermissions("sys:role:select")
    public Result select(){
        Map<String, Object> map = new HashMap<>();
        //如果不是超级管理员，则只查询自己所拥有的角色列表
        if(!Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(getUserName())){
            map.put("create_by",getUserName());
        }
        List<SysRoleEntity> list = sysRoleService.listByMap(map);

        return Result.ok(list);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:role:info")
    public Result<SysRoleEntity> info(@PathVariable("id") Long id){
		SysRoleEntity sysRole = sysRoleService.getById(id);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(id);
        sysRole.setMenuIdList(menuIdList);

        return Result.ok(sysRole);
    }

    /**
     * 保存
     */
    @SysLog("保存角色")
    @ApiOperation("保存")
    @PostMapping("/save")
    @RequiresPermissions("sys:role:save")
    public Result<Object> save(@RequestBody SysRoleEntity sysRole){
		sysRoleService.save(sysRole);

        return Result.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改角色")
    @ApiOperation("修改")
    @PostMapping("/update")
    @RequiresPermissions("sys:role:update")
    public Result<Object> update(@RequestBody SysRoleEntity sysRole){
		sysRoleService.updateById(sysRole);

        return Result.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除角色")
    @ApiOperation("删除")
    @PostMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public Result<Object> delete(@RequestBody Long[] ids){
		sysRoleService.removeByIds(Arrays.asList(ids));

        return Result.ok();
    }

}
