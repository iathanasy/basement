package com.eyxyt.basement.constant;

/**
 * jwt 常量
 * @author cd.wang
 * @create 2020-07-24 11:13
 * @since 1.0.0
 */
public class JwtConstant {

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    public static final String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * JWT-account:
     */
    public static final String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis:
     */
    public static final String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * redis 登录验证码
     */
    public static final String LOGIN_CODE_KEY = "login:code:";
    /**
     * 验证码过期时间
     * 5分钟
     */
    public static final long LOGIN_CODE_EXPIRE = 5 * 60;
}
