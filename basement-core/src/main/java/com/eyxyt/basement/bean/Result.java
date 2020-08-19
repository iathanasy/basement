package com.eyxyt.basement.bean;

import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.enums.ErrorCodeEnum;
import com.eyxyt.basement.enums.ResultEnum;
import com.eyxyt.basement.exception.CustomException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 响应
 * @author cd.wang
 * @create 2020-07-14 17:23
 * @since 1.0.0
 */
@ApiModel
@Data
public class Result<T> {

    /**
     * HTTP状态码
     */
    @ApiModelProperty(name = "code",value = "状态码(200成功，其它均为异常)")
    private int code = ResultEnum.OK.getCode();

    /**
     * 响应消息
     * */
    @ApiModelProperty(name = "msg",value = "响应消息")
    private String msg = ResultEnum.OK.getMsg();

    /**
     * 展示消息
     */
    @ApiModelProperty(name = "text",value = "展示消息")
    private String text = ResultEnum.OK.getText();
    /**
     * 响应中的数据
     * */
    @ApiModelProperty(name = "data",value = "响应数据")
    private T data;

    ////////////////// 成功 //////////////////////////
    public static Result ok(){
        return new Result();
    }

    public static Result ok(String msg){
        return ok(msg, null);
    }

    public static <T> Result ok(T data) {
        Result result = new Result();
        result.setData(data);
        return result;
    }

    public static Result ok(String msg,String text) {
        return ok(msg, text, null);
    }

    public static Result okText(String text) {
        return ok(null, text, null);
    }

    public static Result okStr(String data){
        return ok(null, null, data);
    }

    public static <T> Result ok(String msg, String text, T obj){
        Result result = new Result();
        if(StrUtil.isNotBlank(msg)){
            result.setMsg(msg);
        }
        if(StrUtil.isNotBlank(text)){
            result.setText(text);
        }
        result.setData(obj);
        return result;
    }

    ////////////////// 错误 //////////////////////////
    public static Result error(){
        return error(ResultEnum.ERROR.getCode(), ResultEnum.ERROR.getMsg(), ResultEnum.ERROR.getText());
    }

    public static Result error(String msg){
        return error(ResultEnum.ERROR.getCode(), msg, null);
    }

    public static Result error(String msg, String text){
        return error(ResultEnum.ERROR.getCode(), msg, text);
    }

    public static Result error(Integer code, String msg){
        return error(code, msg, null);
    }

    public static Result error(Integer code, String msg, String text){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setText(text);
        if(StrUtil.isBlank(text)){
            result.setText(ResultEnum.ERROR.getText());
        }
        return result;
    }

    public static Result error(ErrorCodeEnum re){
        Result result = new Result();
        result.setCode(re.getCode());
        result.setMsg(re.getMsg());
        result.setText(re.getText());
        return result;
    }

    public static Result error(ErrorCodeEnum re, String msg){
        Result result = new Result();
        result.setCode(re.getCode());
        result.setMsg(msg);
        result.setText(re.getText());
        return result;
    }

    public static Result error(CustomException ex){
        return error(ex.getCode(), ex.getMessage(), ex.getText());
    }
}
