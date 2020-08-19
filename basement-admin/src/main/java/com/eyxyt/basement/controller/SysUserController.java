package com.eyxyt.basement.controller;

import com.eyxyt.basement.annotation.SysLog;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.bean.QueryParams;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.constant.Constant;
import com.eyxyt.basement.dto.PasswordDto;
import com.eyxyt.basement.entity.SysUserEntity;
import com.eyxyt.basement.service.SysUserRoleService;
import com.eyxyt.basement.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 系统用户
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 17:51:12
 */
@Api(tags = "系统用户 管理")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController{

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @RequiresPermissions("sys:user:list")
    public Result<PageVo> list(QueryParams params) {

        //如果不是超级管理员，则只查询自己创建的角色列表
        if(!Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(getUserName())){
            params.setCreateBy(getUserName());
        }
        PageVo page = sysUserService.queryPage(new Query(params));

        return Result.ok(page);
    }


    /**
     * 获取登录的用户信息
     */
    @ApiOperation("获取登录的用户信息")
    @GetMapping("/info")
    public Result info(){
        return Result.ok(getUser());
    }

    /**
     * 修改登录用户密码
     */
    @ApiOperation("修改密码")
    @SysLog("修改密码")
    @PostMapping("/password")
    public Result password(@RequestBody PasswordDto form){
        //sha256加密
        String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
        //sha256加密
        String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if(!flag){
            return Result.error("原密码不正确");
        }
        return Result.ok();
    }

    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{id}")
    @RequiresPermissions("sys:user:info")
    public Result<SysUserEntity> info(@PathVariable("id") Long id){
        SysUserEntity user = sysUserService.getById(id);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(id);
        user.setRoleIdList(roleIdList);

        return Result.ok(user);
    }

    /**
     * 保存
     */
    @SysLog("保存用户")
    @ApiOperation("保存")
    @PostMapping("/save")
    @RequiresPermissions("sys:user:save")
    public Result<Object> save(@RequestBody SysUserEntity sysUser){
		sysUserService.save(sysUser);

        return Result.ok();
    }

    /**
     * 修改
     */
    @SysLog("修改用户")
    @ApiOperation("修改")
    @PostMapping("/update")
    @RequiresPermissions("sys:user:update")
    public Result update(@RequestBody SysUserEntity sysUser){
		sysUserService.updateById(sysUser);

        return Result.ok();
    }

    /**
     * 删除
     */
    @SysLog("删除用户")
    @ApiOperation("删除")
    @PostMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public Result delete(@RequestBody Long[] ids){
        if(ArrayUtils.contains(ids, Constant.SUPER_ADMIN_ID)){
            return Result.error("系统管理员不能删除");
        }

        if(ArrayUtils.contains(ids, getUserId())){
            return Result.error("当前用户不能删除");
        }

		sysUserService.deleteBatch(ids);

        return Result.ok();
    }

}
