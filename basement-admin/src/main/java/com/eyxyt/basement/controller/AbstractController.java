package com.eyxyt.basement.controller;

import com.eyxyt.basement.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;

/**
 * @author cd.wang
 * @create 2020-07-23 17:57
 * @since 1.0.0
 */
public abstract class AbstractController {

    protected SysUserEntity getUser() {
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getUserId() {
        return getUser().getId();
    }

    protected String getUserName(){
        return getUser().getUsername();
    }
}
