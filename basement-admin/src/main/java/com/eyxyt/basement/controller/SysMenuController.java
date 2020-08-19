package com.eyxyt.basement.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.annotation.SysLog;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.constant.Constant;
import com.eyxyt.basement.entity.SysMenuEntity;
import com.eyxyt.basement.exception.CustomException;
import com.eyxyt.basement.service.ShiroService;
import com.eyxyt.basement.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * 菜单管理
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 17:51:12
 */
@Api(tags = "菜单管理 管理")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController{
    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private ShiroService shiroService;

    /**
     * 导航菜单
     */
    @ApiOperation("左侧导航菜单")
    @GetMapping("/nav")
    public Result nav(){
        List<SysMenuEntity> menuList = sysMenuService.getUserMenuListByUserName(getUserName());
        Set<String> permissions = shiroService.getUserPermissions(getUserName());
        HashMap<String, Object> map = MapUtil.newHashMap();
        map.put("menuList", menuList);
        map.put("permissions", permissions);

        return Result.ok(map);
    }


    /**
     * 所有菜单列表
     */
    @ApiOperation("所有菜单列表")
    @GetMapping("/list")
    @RequiresPermissions("sys:menu:list")
    public Result list(){
        List<SysMenuEntity> menuList = sysMenuService.list();
        for(SysMenuEntity sysMenuEntity : menuList){
            SysMenuEntity parentMenuEntity = sysMenuService.getById(sysMenuEntity.getParentId());
            if(parentMenuEntity != null){
                sysMenuEntity.setParentName(parentMenuEntity.getName());
            }
        }

        return Result.ok(menuList);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @ApiOperation("选择菜单")
    @GetMapping("/select")
    @RequiresPermissions("sys:menu:select")
    public Result select(){
        //查询列表数据
        List<SysMenuEntity> menuList = sysMenuService.queryNotButtonList();

        //添加顶级菜单
        SysMenuEntity root = new SysMenuEntity();
        root.setId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return Result.ok(menuList);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:menu:info")
    public Result<SysMenuEntity> info(@PathVariable("id") Long id){
		SysMenuEntity sysMenu = sysMenuService.getById(id);

        return Result.ok(sysMenu);
    }

    /**
     * 保存
     */
    @SysLog("保存菜单")
    @ApiOperation("保存")
    @PostMapping("/save")
    @RequiresPermissions("sys:menu:save")
    public Result save(@RequestBody SysMenuEntity sysMenu){
        //数据校验
        verifyForm(sysMenu);

		sysMenuService.save(sysMenu);

        return Result.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改菜单")
    @ApiOperation("修改")
    @PostMapping("/update")
    @RequiresPermissions("sys:menu:update")
    public Result update(@RequestBody SysMenuEntity sysMenu){
        //数据校验
        verifyForm(sysMenu);
		sysMenuService.updateById(sysMenu);

        return Result.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除菜单")
    @ApiOperation("修改")
    @PostMapping("/delete/{id}")
    @RequiresPermissions("sys:menu:delete")
    public Result delete(@PathVariable("id") long id){
        if(id <= 31){
            return Result.error("系统菜单，不能删除");
        }

        //判断是否有子菜单或按钮
        List<SysMenuEntity> menuList = sysMenuService.queryListParentId(id);
        if(menuList.size() > 0){
            return Result.error("请先删除子菜单或按钮");
        }

        sysMenuService.delete(id);

        return Result.ok();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenuEntity menu){
        if(StrUtil.isBlank(menu.getName())){
            throw new CustomException("菜单名称不能为空");
        }

        if(menu.getParentId() == null){
            throw new CustomException("上级菜单不能为空");
        }

        //菜单
        if(menu.getType() == Constant.MenuType.MENU.getValue()){
            if(StrUtil.isBlank(menu.getUrl())){
                throw new CustomException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if(menu.getParentId() != 0){
            SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()){
            if(parentType != Constant.MenuType.CATALOG.getValue()){
                throw new CustomException("上级菜单只能为目录类型");
            }
            return ;
        }

        //按钮
        if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
            if(parentType != Constant.MenuType.MENU.getValue()){
                throw new CustomException("上级菜单只能为菜单类型");
            }
            return ;
        }
    }

}
