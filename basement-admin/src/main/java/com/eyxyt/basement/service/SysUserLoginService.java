package com.eyxyt.basement.service;

import com.eyxyt.basement.dto.SysUserLoginDto;

import java.awt.image.BufferedImage;

/**
 * @author cd.wang
 * @create 2020-07-24 14:37
 * @since 1.0.0
 */
public interface SysUserLoginService {

    /**
     * 登陆
     * @param userLoginDto
     * @return
     */
    String login(SysUserLoginDto userLoginDto);

    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
