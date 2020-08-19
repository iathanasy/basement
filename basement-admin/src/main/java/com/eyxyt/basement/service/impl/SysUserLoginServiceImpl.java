package com.eyxyt.basement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.config.redis.RedisUtil;
import com.eyxyt.basement.constant.JwtConstant;
import com.eyxyt.basement.dto.SysUserLoginDto;
import com.eyxyt.basement.entity.SysUserEntity;
import com.eyxyt.basement.exception.CustomException;
import com.eyxyt.basement.service.SysUserLoginService;
import com.eyxyt.basement.service.SysUserService;
import com.eyxyt.basement.shiro.jwt.JwtProperties;
import com.eyxyt.basement.shiro.jwt.JwtUtils;
import com.eyxyt.basement.utils.CaptchaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * @author cd.wang
 * @create 2020-07-24 14:44
 * @since 1.0.0
 */
@Slf4j
@Service
public class SysUserLoginServiceImpl implements SysUserLoginService {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private JwtProperties jwt;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public String login(SysUserLoginDto userLoginDto) {
        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(userLoginDto.getUsername());

        //账号不存在、密码错误
        if(user == null || !user.getPassword().equals(new Sha256Hash(userLoginDto.getPassword(), user.getSalt()).toHex())) {
            throw new CustomException("账号或密码不正确");
        }

        //账号锁定
        if(user.getStatus() == 0){
            throw new CustomException("账号已被锁定,请联系管理员");
        }

        // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        redisUtil.set(JwtConstant.PREFIX_SHIRO_REFRESH_TOKEN + user.getUsername(), currentTimeMillis, jwt.getRefreshExpire());
        // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
        String token = JwtUtils.sign(jwt, user.getUsername(), currentTimeMillis);

        return token;
    }

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StrUtil.isBlank(uuid))
            throw new CustomException("uuid不能为空");

        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CaptchaUtils.Captcha captcha = CaptchaUtils.createCaptcha(140, 38, 4, 10, 30);
        String code = captcha.getCode();
        redisUtil.set(JwtConstant.LOGIN_CODE_KEY + uuid, code, JwtConstant.LOGIN_CODE_EXPIRE);
        return captcha.getImage();
    }

    @Override
    public boolean validate(String uuid, String code) {
       String rcode = redisUtil.get(JwtConstant.LOGIN_CODE_KEY + uuid).toString();
        log.warn("code:{},rcode:{},uuid:{}",code, rcode, uuid);
        if (StrUtil.isBlank(rcode)) {
            return false;
        }

        if(rcode.equalsIgnoreCase(code)){
            redisUtil.del(JwtConstant.LOGIN_CODE_KEY + uuid);
            return true;
        }
        return false;
    }
}
