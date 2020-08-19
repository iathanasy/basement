package com.eyxyt.basement.service;

import com.eyxyt.basement.entity.SysUserEntity;

import java.util.Set;

/**
 * shiro相关接口
 * @author cd.wang
 * @create 2020-07-23 17:59
 * @since 1.0.0
 */
public interface ShiroService {

    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(String userName);


    /**
     * 根据用户名，查询用户
     * @param userName
     */
    SysUserEntity queryUser(String userName);
}
