package com.eyxyt.basement.handler;

import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.bean.Result;
import com.eyxyt.basement.enums.ResultEnum;
import com.eyxyt.basement.exception.CustomException;
import com.eyxyt.basement.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;
import java.util.StringJoiner;

/**
 * 异常控制处理器
 * @author cd.wang
 * @create 2020-07-15 14:39
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 捕捉其他所有自定义异常
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public Result handle(CustomException e) {
        if(StrUtil.isBlank(e.getText())){
            e.setText(ResultEnum.ERROR.getText());
        }
        return Result.error(e);
    }

    /**
     * 捕捉401 未授权异常
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Result handle(UnauthorizedException e) {
        return Result.error(ResultEnum.UNAUTHORIZED.getCode(), e.getMessage(), ResultEnum.UNAUTHORIZED.getText());
    }

    /**
     * 捕捉所有Shiro异常
     * @param e
     * @return
     */
    @ExceptionHandler(ShiroException.class)
    public Result handle401(ShiroException e) {
        log.error("Shiro 异常 (" + e.getMessage() + ")");
        return Result.error(ResultEnum.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationException.class)
    public Result handleAuthorizationException(AuthorizationException e){
        log.error("Token 异常 (" + e.getMessage() + ")");
        return Result.error(ResultEnum.UNAUTHORIZED);
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(org.apache.shiro.authz.UnauthorizedException.class)
    public Result handle401(org.apache.shiro.authz.UnauthorizedException e) {
        log.error("无权访问(Unauthorized):当前Subject没有此请求所需权限(" + e.getMessage() + ")");
        return Result.error(ResultEnum.UNAUTHORIZED);
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    public Result handle401(UnauthenticatedException e) {
        log.error("无权访问(Unauthorized):当前Subject是匿名Subject，请先登录(This subject is anonymous.)");
        return Result.error(ResultEnum.UNAUTHORIZED);
    }

    /**
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        // 按需重新封装需要返回的错误信息 解析原错误信息，
        // 封装后返回，此处返回非法的字段名称error.getField()，
        // 原始值error.getRejectedValue()，错误信息
        StringJoiner sj = new StringJoiner(";");
        e.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        return Result.error(ResultEnum.PARAM_ERROR, sj.toString());
    }


    /**
     * ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public Result handleValidationException(ValidationException e) {
        //log.error(e.getMessage(), e);
        return Result.error(ResultEnum.PARAM_ERROR, e.getCause().getMessage());
    }

    /**
     * ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        //log.error(e.getMessage(), e);
        StringJoiner sj = new StringJoiner(";");
        e.getConstraintViolations().forEach(x -> sj.add(x.getMessageTemplate()));
        return Result.error(ResultEnum.PARAM_ERROR, sj.toString());
    }

    /**
     * 方法请求异常
     * @param e
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Result.error(ResultEnum.ERROR, "不支持'" + e.getMethod() + "'请求方法");
    }
    /**
     * 捕捉404异常
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result handle(NoHandlerFoundException e) {
        return Result.error(ResultEnum.NOT_FOUND);
    }

    /**
     * 捕捉数据库异常
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handle(DuplicateKeyException e) {
        return Result.error(ResultEnum.DATA_EXISTS);
    }

    /**
     * 捕捉其他所有异常
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result globalException(HttpServletRequest request, Throwable ex) {
        log.error("--"+ this.getClass().getSimpleName()+"--> " + ex.getMessage());
        ex.printStackTrace();
        return Result.error(this.getStatus(request).value(), ex.toString() + ": " + ex.getMessage(), ResultEnum.ERROR.getText());
    }


    /**
     * 获取状态码
     * @param request
     * @return
     */
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
