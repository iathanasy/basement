package com.eyxyt.basement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eyxyt.basement.entity.SysUserRoleEntity;

import java.util.List;

/**
 * 用户与角色对应关系
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRoleEntity> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);


    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
