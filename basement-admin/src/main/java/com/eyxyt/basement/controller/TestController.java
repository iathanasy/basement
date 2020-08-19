package com.eyxyt.basement.controller;

import com.eyxyt.basement.bean.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cd.wang
 * @create 2020-07-25 15:49
 * @since 1.0.0
 */
@Api(tags = "系统测试 管理")
@RestController
@RequestMapping("/sys/test")
public class TestController {

    @ApiOperation("测试登录")
    @GetMapping("/article")
    public Result article() {
        Subject subject = SecurityUtils.getSubject();
        // 登录了返回true
        if (subject.isAuthenticated()) {
            return Result.ok("您已经登录了(You are already logged in)");
        } else {
            return Result.ok("你是游客(You are guest)");
        }
    }

    @ApiOperation("测试权限")
    @GetMapping("/view")
    @RequiresPermissions(logical = Logical.AND, value = {"user:view"})
    public Result view() {
        return Result.ok("展示(view)");
    }
}
