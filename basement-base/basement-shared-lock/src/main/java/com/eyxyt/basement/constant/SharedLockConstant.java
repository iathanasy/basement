package com.eyxyt.basement.constant;

/**
 * 常量
 * @author cd.wang
 * @create 2020-07-15 16:47
 * @since 1.0.0
 */
public class SharedLockConstant {

    /**
     * redis-key-前缀-refresh_token:
     */
    public static final String PREFIX_REFRESH_TOKEN = "refresh_token:";

    /**
     * JWT-account:
     */
    public static final String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis:
     */
    public static final String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * session / request
     */
    public static final String USER_SESSION = "user";
}
