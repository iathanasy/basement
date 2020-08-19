package com.eyxyt.basement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.SysRoleEntity;

import java.util.List;


/**
 * 角色
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysRoleService extends IService<SysRoleEntity> {


    PageVo queryPage(Query params);


    void insert(SysRoleEntity role);

    void update(SysRoleEntity role);

    void deleteBatch(Long[] roleIds);


    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(String createUserName);
}

