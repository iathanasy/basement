package com.eyxyt.basement.dto;

import lombok.Data;

/**
 * @author cd.wang
 * @create 2020-07-24 14:40
 * @since 1.0.0
 */
@Data
public class SysUserLoginDto {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
}
