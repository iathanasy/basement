package com.eyxyt.basement.shiro.jwt;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.eyxyt.basement.config.redis.RedisUtil;
import com.eyxyt.basement.constant.JwtConstant;
import com.eyxyt.basement.exception.UnauthorizedException;
import com.eyxyt.basement.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT过滤
 * @author cd.wang
 * @create 2020-07-24 9:16
 * @since 1.0.0
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    private JwtProperties jwt;

    private RedisUtil redisUtil;


    /**
     *  只能注入一次
     *  此处是处理 filter 抛出的异常不能直接被 异常处理器(CustomExceptionHandler) 捕获到
     */
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;


    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);;

        // 查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        if (this.isLoginAttempt(request, response)) {
            try {
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }

                // Token认证失败直接返回Response信息
                resolver.resolveException(httpRequest, httpResponse, null, new UnauthorizedException(msg));
                return false;
            }
        } else {
            // 没有携带Token
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            // 获取当前请求类型
            String httpMethod = httpServletRequest.getMethod();
            // 获取当前请求URI
            String requestURI = httpServletRequest.getRequestURI();
            log.info("当前请求 {} Authorization属性(Token)为空 请求类型 {}", requestURI, httpMethod);
            resolver.resolveException(httpRequest, httpResponse, null, new UnauthorizedException(jwt.getHeaderKey() + "不能为空"));
        }
        return true;
    }

    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getToken(request);
        return StrUtil.isNotBlank(token);
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        JwtToken token = new JwtToken(this.getToken(request));
        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (HttpMethod.OPTIONS.equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        //属性初始化
        init();

        return super.preHandle(request, response);
    }

    /**
     * 从 Header 或 parameter 中获取 Token
     * @param servletRequest
     * @return
     */
    private String getToken(ServletRequest servletRequest){
        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        String token = request.getHeader(jwt.getHeaderKey());
        if(StrUtil.isBlank(token)){
            token = request.getParameter(jwt.getHeaderKey());
        }
        if(StrUtil.isBlank(token) || !token.startsWith(jwt.getTokenPrefix())){
            return null;
        }

        //替换Bearer+;
        token = token.replace(jwt.getTokenPrefix(), "");
        return token;
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getToken(request);
        // 获取当前Token的帐号信息
        String account = JwtUtils.getClaim(token, JwtConstant.ACCOUNT);
        // 判断Redis中RefreshToken是否存在
        if (redisUtil.hasKey(JwtConstant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisUtil.get(JwtConstant.PREFIX_SHIRO_REFRESH_TOKEN + account).toString();
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (JwtUtils.getClaim(token, JwtConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                // 获取当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                Long refreshTokenExpireTime = jwt.getRefreshExpire();
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                redisUtil.set(JwtConstant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis, refreshTokenExpireTime);
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                token = JwtUtils.sign(jwt,account, currentTimeMillis);
                // 将新刷新的AccessToken再次进行Shiro的登录
                JwtToken jwtToken = new JwtToken(token);
                // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                this.getSubject(request, response).login(jwtToken);

                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.setHeader(jwt.getHeaderKey(), jwt.getTokenPrefix() + token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", jwt.getHeaderKey());
                return true;
            }
        }
        return false;
    }


    /**
     *
     * 初始化注入
     */
    private void init(){
        if (jwt == null) {
            jwt = SpringContextUtils.getBean("jwtProperties", JwtProperties.class);
        }

        if(redisUtil == null){
            redisUtil = SpringContextUtils.getBean("redisUtil", RedisUtil.class);
        }

        if(resolver == null){
            resolver = SpringContextUtils.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
        }
    }

}
