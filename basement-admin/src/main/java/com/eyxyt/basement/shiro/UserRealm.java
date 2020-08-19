package com.eyxyt.basement.shiro;

import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.config.redis.RedisUtil;
import com.eyxyt.basement.constant.Constant;
import com.eyxyt.basement.constant.JwtConstant;
import com.eyxyt.basement.entity.SysUserEntity;
import com.eyxyt.basement.exception.UnauthorizedException;
import com.eyxyt.basement.service.ShiroService;
import com.eyxyt.basement.shiro.jwt.JwtProperties;
import com.eyxyt.basement.shiro.jwt.JwtToken;
import com.eyxyt.basement.shiro.jwt.JwtUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 自定义Realm 认证
 * @author cd.wang
 * @create 2020-07-24 9:13
 * @since 1.0.0
 */
@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private JwtProperties jwt;
    @Autowired
    private ShiroService shiroService;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     *  授权(验证权限时调用)
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
//        String account = JwtUtils.getClaim(getName(), JwtConstant.ACCOUNT);
        String account = user.getUsername();
        //用户权限列表
        Set<String> permsSet = shiroService.getUserPermissions(account);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();

        setName(token);

        // 解密获得account，用于和数据库进行对比
        String account = JwtUtils.getClaim(token, JwtConstant.ACCOUNT);

        // 帐号为空
        if (StrUtil.isBlank(account)) {
            throw new AuthenticationException("Token中帐号为空");
        }

        //查询用户信息
        SysUserEntity user = shiroService.queryUser(account);
        //账号锁定
        if(user.getStatus() == 0){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtils.verify(jwt,token) && redisUtil.hasKey(JwtConstant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisUtil.get(JwtConstant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtils.getClaim(token, JwtConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(user, token, getName());
            }
        }
        throw new AuthenticationException("Token已过期");
    }

    /**
     * 超级管理员拥有所有权限
     */
    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        return Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(user.getUsername()) || super.isPermitted(principals, permission);
    }

    /**
     * 超级管理员拥有所有角色
     */
    @Override
    public boolean hasRole(PrincipalCollection principals, String roleIdentifier) {
        SysUserEntity user = (SysUserEntity)principals.getPrimaryPrincipal();
        return Constant.SUPER_ADMIN_NAME.equalsIgnoreCase(user.getUsername()) || super.hasRole(principals, roleIdentifier);
    }
}
