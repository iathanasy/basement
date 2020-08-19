package com.eyxyt.basement.annotation;

import java.lang.annotation.*;

/**
 * 登录校验 如果不需要TOKEN就可以访问API,可以加上此注解
 * 标识是否忽略REST安全性检查
 * @author cd.wang
 * @create 2020-07-15 17:52
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreSecurity {

}
