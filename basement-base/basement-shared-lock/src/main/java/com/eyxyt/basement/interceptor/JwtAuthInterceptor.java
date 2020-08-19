package com.eyxyt.basement.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.eyxyt.basement.annotation.IgnoreSecurity;
import com.eyxyt.basement.config.redis.RedisUtil;
import com.eyxyt.basement.constant.SharedLockConstant;
import com.eyxyt.basement.entity.biz.BizAppUserEntity;
import com.eyxyt.basement.exception.UnauthorizedException;
import com.eyxyt.basement.properties.JwtProperties;
import com.eyxyt.basement.service.BizAppUserService;
import com.eyxyt.basement.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt授权拦截器
 * @author cd.wang
 * @create 2020-07-16 9:42
 * @since 1.0.0
 */
@Slf4j
@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwt;
    @Autowired
    private RedisUtil redisUtil;


    @Autowired
    private BizAppUserService userService;

    /**
     * 此处是处理 filter 抛出的异常不能直接被 异常处理器(CustomExceptionHandler) 捕获到
     */
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 忽略带IgnoreSecurity注解的请求, 不做后续token认证校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            IgnoreSecurity ignoreSecurity = handlerMethod.getMethodAnnotation(IgnoreSecurity.class);
            if (ignoreSecurity != null) {
                return true;
            }
        }

        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (HttpMethod.OPTIONS.equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        //关闭验证直接忽略
        if(!jwt.isOpen()){
            return true;
        }

        //查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        String token = this.getToken(request);

        if (StrUtil.isBlank(token) || !token.startsWith(jwt.getTokenPrefix())) {
            resolver.resolveException(request, response, null, new UnauthorizedException(jwt.getHeaderKey() + "不能为空"));
            return false;
        }

        //替换Bearer+;
        token = token.replace(jwt.getTokenPrefix(), "");

        try {
            //开始认证
            boolean verify = JwtUtils.verify(jwt, token);
            if (!verify) {
                resolver.resolveException(request, response, null, new UnauthorizedException("Token已过期"));
                return false;
            }

            // 解密获得account，用于和数据库进行对比
            String account = JwtUtils.getClaim(token, SharedLockConstant.ACCOUNT);
            // 帐号为空
            if (StrUtil.isBlank(account)) {
                resolver.resolveException(request, response, null, new UnauthorizedException("Token中帐号为空"));
                return false;
            }

            //比对数据库
            BizAppUserEntity userEntity = userService.selectOne(account);

            request.setAttribute(SharedLockConstant.USER_SESSION, userEntity);

            return true;
        } catch (Exception e) {
            // 认证出现异常，传递错误信息msg
            String msg = e.getMessage();
            // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
            Throwable throwable = e.getCause();
            if (e instanceof SignatureVerificationException) {
                log.error("Token或者密钥不正确(" + msg + ")");
                // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                msg = "Token或者密钥不正确";
            } else if (e instanceof TokenExpiredException) {
                log.error("Token已过期(" + msg + "),尝试自动刷新Token");
                // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                if (this.refreshToken(token, response)) {
                    return true;
                }
                msg = "Token已过期";
            } else {
                // 应用异常不为空
                if (throwable != null) {
                    // 获取应用异常msg
                    msg = throwable.getMessage();
                }
            }
            resolver.resolveException(request, response, null, new UnauthorizedException(msg));
            return false;
        }
    }


    /**
     * 从 Header 或 parameter 中获取 Token
     * @param request
     * @return
     */
    private String getToken(HttpServletRequest request){
        String token = request.getHeader(jwt.getHeaderKey());
        if(StrUtil.isBlank(token)){
            token = request.getParameter(jwt.getHeaderKey());
        }
        return token;
    }


    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(String token, HttpServletResponse response) {
        // 获取当前Token的帐号信息
        String account = JwtUtils.getClaim(token, SharedLockConstant.ACCOUNT);
        // 判断Redis中RefreshToken是否存在
        if (redisUtil.hasKey(SharedLockConstant.PREFIX_REFRESH_TOKEN + account)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisUtil.get(SharedLockConstant.PREFIX_REFRESH_TOKEN + account).toString();
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (JwtUtils.getClaim(token, SharedLockConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                // 获取当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                Long refreshTokenExpireTime = jwt.getRefreshExpire();
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                redisUtil.set(SharedLockConstant.PREFIX_REFRESH_TOKEN + account, currentTimeMillis, refreshTokenExpireTime);
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JwtUtils.sign(jwt, account, currentTimeMillis);

                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                response.setHeader(jwt.getHeaderKey(), jwt.getTokenPrefix() + token);
                response.setHeader("Access-Control-Expose-Headers", jwt.getHeaderKey());
                return true;
            }
        }
        return false;
    }
}
