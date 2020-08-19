package com.eyxyt.basement.dto;

import lombok.Data;

/**
 * 修改密码
 * @author cd.wang
 * @create 2020-07-23 19:24
 * @since 1.0.0
 */
@Data
public class PasswordDto{

    /**
     * 账号
     */
    private String userName;
    /**
     * 原密码
     */
    private String password;
    /**
     * 新密码
     */
    private String newPassword;

}
