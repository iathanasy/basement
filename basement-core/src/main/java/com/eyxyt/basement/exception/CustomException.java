package com.eyxyt.basement.exception;

import com.eyxyt.basement.enums.ResultEnum;

/**
 * 自定义运行时异常
 *
  * @author cd.wang
 * @create 2020-07-14 17:18
 * @since 1.0.0
 */
public class CustomException extends RuntimeException{

    private Integer code = ResultEnum.ERROR.getCode();
    private String msg;
    private String text;

    public CustomException(String msg)
    {
        this.msg = msg;
    }

    public CustomException(String msg, String text)
    {
        this.msg = msg;
        this.text = text;
    }

    public CustomException(String msg, Integer code)
    {
        this.msg = msg;
        this.code = code;
    }

    public CustomException(String msg, Integer code, String text)
    {
        this.msg = msg;
        this.code = code;
        this.text = text;
    }

    public CustomException(String msg, Throwable e)
    {
        super(msg, e);
        this.msg = msg;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getMessage()
    {
        return msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getText(){
        return text;
    }
}
