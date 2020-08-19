package com.eyxyt.basement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.SysMenuEntity;

import java.util.List;


/**
 * 菜单管理
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     * @param menuIdList  用户菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuEntity> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<SysMenuEntity> getUserMenuList(Long userId);
    List<SysMenuEntity> getUserMenuListByUserName(String userName);

    /**
     * 删除
     */
    void delete(Long menuId);
}

