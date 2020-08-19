package com.eyxyt.basement.exception;

/**
 *  未授权异常
 * @author cd.wang
 * @create 2020-07-16 9:10
 * @since 1.0.0
 */
public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

}
