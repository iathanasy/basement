package com.eyxyt.basement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eyxyt.basement.entity.SysRoleEntity;

import java.util.List;

/**
 * 角色
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysRoleMapper extends BaseMapper<SysRoleEntity> {

    /**
     * 查询用户创建的角色ID列表
     */
    List<Long> queryRoleIdList(String createUserName);
}
