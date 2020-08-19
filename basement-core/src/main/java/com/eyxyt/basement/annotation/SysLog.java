package com.eyxyt.basement.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * @author cd.wang
 * @create 2020-07-23 14:34
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
