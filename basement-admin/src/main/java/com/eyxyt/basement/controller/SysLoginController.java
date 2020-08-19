package com.eyxyt.basement.controller;

import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.dto.SysUserLoginDto;
import com.eyxyt.basement.service.SysUserLoginService;
import com.eyxyt.basement.shiro.jwt.JwtProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * 系统后台登陆
 * @author cd.wang
 * @create 2020-07-24 14:29
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "系统登陆 管理")
@RestController
public class SysLoginController extends AbstractController{

    @Autowired
    private SysUserLoginService userLoginService;

    @Autowired
    private JwtProperties jwt;

    @Value("${shiro.login.verify}")
    private boolean loginVerify;

    @ApiOperation("是否显示验证码")
    @GetMapping("/loginVerify")
    public Result loginVerify(){
        return Result.ok(loginVerify);
    }

    @ApiOperation("验证码")
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, String uuid) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/png");

        BufferedImage bufferedImage = userLoginService.getCaptcha(uuid);

        OutputStream os = response.getOutputStream();
        ImageIO.write(bufferedImage, "png", os);
    }

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public Result delete(@RequestBody SysUserLoginDto userLoginDto, HttpServletResponse response){
        // 如果开启了登录校验
        if (loginVerify) {
            boolean validate = userLoginService.validate(userLoginDto.getUuid(), userLoginDto.getCaptcha());
            if(!validate){
                return Result.error("验证码错误","验证码错误");
            }
        }
        String token = userLoginService.login(userLoginDto);

        response.setHeader(jwt.getHeaderKey(), jwt.getTokenPrefix() + token);
        response.setHeader("Access-Control-Expose-Headers", jwt.getHeaderKey());

        HashMap<String, Object> map = new HashMap<String, Object>(2);
        map.put(jwt.getHeaderKey(), jwt.getTokenPrefix() + token);
        map.put("expire", jwt.getExpire());
        return Result.ok(map);
    }

    @ApiOperation("注销")
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.ok();
    }

}
