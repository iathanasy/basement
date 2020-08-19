package com.eyxyt.basement.service.impl;

import com.eyxyt.basement.constant.Constant;
import com.eyxyt.basement.entity.SysMenuEntity;
import com.eyxyt.basement.entity.SysUserEntity;
import com.eyxyt.basement.mapper.SysMenuMapper;
import com.eyxyt.basement.mapper.SysUserMapper;
import com.eyxyt.basement.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author cd.wang
 * @create 2020-07-23 18:01
 * @since 1.0.0
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Set<String> getUserPermissions(String userName) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(userName)){
            List<SysMenuEntity> menuList = sysMenuMapper.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for(SysMenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = sysUserMapper.queryAllPermsByUserName(userName);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public SysUserEntity queryUser(String userName) {
        return sysUserMapper.queryByUserName(userName);
    }
}
