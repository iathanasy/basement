package com.eyxyt.basement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.constant.Constant;
import com.eyxyt.basement.entity.SysRoleEntity;
import com.eyxyt.basement.exception.CustomException;
import com.eyxyt.basement.mapper.SysRoleMapper;
import com.eyxyt.basement.service.SysRoleMenuService;
import com.eyxyt.basement.service.SysRoleService;
import com.eyxyt.basement.service.SysUserRoleService;
import com.eyxyt.basement.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public PageVo queryPage(Query params) {
        String roleName = params.getParams().getKey();
        String createBy = params.getParams().getCreateBy();

        Page page = this.page(params.getPage(),
                new QueryWrapper<SysRoleEntity>()
                        .like(StrUtil.isNotBlank(roleName),"role_name", roleName)
                        .eq(StrUtil.isNotBlank(createBy),"create_by", createBy));
        return new PageVo(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(SysRoleEntity role) {
        this.save(role);

        //检查权限是否越权
        checkPrems(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleEntity role) {
        this.updateById(role);

        //检查权限是否越权
        checkPrems(role);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getId(), role.getMenuIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }


    @Override
    public List<Long> queryRoleIdList(String createUserName) {
        return baseMapper.queryRoleIdList(createUserName);
    }

    /**
     * 检查权限是否越权
     */
    private void checkPrems(SysRoleEntity role){
        //如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
        if(Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(role.getCreateBy())){
            return ;
        }

        //查询用户所拥有的菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuIdByUserName(role.getCreateBy());

        //判断是否越权
        if(!menuIdList.containsAll(role.getMenuIdList())){
            throw new CustomException("新增角色的权限，已超出你的权限范围");
        }
    }

}