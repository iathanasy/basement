package com.eyxyt.basement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eyxyt.basement.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * 角色与菜单对应关系
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenuEntity> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
