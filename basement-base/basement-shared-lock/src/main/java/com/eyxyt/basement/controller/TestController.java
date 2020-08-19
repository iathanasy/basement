package com.eyxyt.basement.controller;

import com.eyxyt.basement.annotation.IgnoreSecurity;
import com.eyxyt.basement.annotation.validation.Update;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.bean.QueryParams;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.config.redis.RedisUtil;
import com.eyxyt.basement.constant.SharedLockConstant;
import com.eyxyt.basement.entity.biz.BizAppUserEntity;
import com.eyxyt.basement.exception.CustomException;
import com.eyxyt.basement.model.dto.UserAuthDeviceDto;
import com.eyxyt.basement.model.dto.UserListDto;
import com.eyxyt.basement.properties.JwtProperties;
import com.eyxyt.basement.service.BizAppUserService;
import com.eyxyt.basement.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author cd.wang
 * @create 2020-07-15 11:36
 * @since 1.0.0
 */
@Api(value = "测试", tags = "test")
@RestController
public class TestController {

    @Autowired
    private JwtProperties jwt;
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BizAppUserService userService;

    @ApiOperation(value = "测试")
    @GetMapping("test")
    public Result test(){
        return Result.ok("nice");
    }

    @ApiOperation(value = "异常")
    @GetMapping("ex")
    public Result ex(){
        if(1==1) {
            throw new CustomException("故意抛出");
        }
        return Result.ok();
    }

    @ApiOperation(value = "用户登录")
    @IgnoreSecurity
    @GetMapping("login")
    public Result login(HttpServletResponse response){
        // 查询数据库中的帐号信息
        String mobile = "17313086089";
        userService.selectOne(mobile);

        // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        Long refreshTokenExpireTime = jwt.getRefreshExpire();
        redisUtil.set(SharedLockConstant.PREFIX_REFRESH_TOKEN + mobile, currentTimeMillis, refreshTokenExpireTime);

        String token = JwtUtils.sign(jwt, mobile, currentTimeMillis);

        response.setHeader(jwt.getHeaderKey(), jwt.getTokenPrefix() + token);
        response.setHeader("Access-Control-Expose-Headers", jwt.getHeaderKey());

        HashMap<String, Object> map = new HashMap<String, Object>(2);
        map.put(jwt.getHeaderKey(), jwt.getTokenPrefix() + token);
        map.put("expire", jwt.getExpire());
        return Result.ok(map);
    }

    /**
     * 查询在线用户
     * @param response
     * @return
     */
    @ApiOperation(value = "在线用户")
    @GetMapping("user/online")
    public Result<List<BizAppUserEntity>> online(HttpServletResponse response){
        List<Object> userDtos = new ArrayList<Object>();
        // 查询所有Redis键
        Set<String> keys = redisUtil.keys(SharedLockConstant.PREFIX_REFRESH_TOKEN + "*");
        for (String key : keys) {
            if(redisUtil.hasKey(key)){
                // 根据:分割key，获取最后一个字符(帐号)
                String[] strArray = key.split(":");
                String mobile = strArray[strArray.length - 1];
                BizAppUserEntity userEntity = userService.selectOne(mobile);
                // 设置登录时间
                userEntity.setLoginTime(new Date(Long.parseLong(redisUtil.get(key).toString())));
                userDtos.add(userEntity);
            }
        }

        if (userDtos == null || userDtos.size() < 0) {
            throw new CustomException("查询失败(Query Failure)");
        }
        return Result.ok(userDtos);
    }


    /**
     * 查询用户列表
     * @param params
     * @return
     */
    @ApiOperation(value = "所有用户")
    @PostMapping("user")
    public Result<PageVo> user(@RequestBody QueryParams params){
        System.out.println(params);
        PageVo pageVo = userService.queryPage(new Query(params));
        return Result.ok(pageVo);
    }

    /**
     * all
     * @param userAuth
     * @return
     */
    @ApiOperation(value = "用户授权设备")
    @PostMapping("user/auth/device")
    public Result userAuthDevice(@Validated(Update.class) @RequestBody UserAuthDeviceDto userAuth){

        return Result.ok();
    }

    /**
     * all
     * @param userListDto
     * @return
     */
    @ApiOperation(value = "用户列表")
    @PostMapping("user/list")
    public Result userList(@Validated @RequestBody UserListDto userListDto){
        PageVo pageVo = userService.selectPage(new Query(userListDto));
        return Result.ok(pageVo);
    }


}
