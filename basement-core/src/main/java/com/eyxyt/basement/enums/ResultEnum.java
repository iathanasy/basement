package com.eyxyt.basement.enums;
/**
 * 系统枚举
 * @author cd.wang
 * @create 2020-07-14 17:25
 * @since 1.0.0
 */
public enum ResultEnum implements ErrorCodeEnum {

    /**
     * 请求成功
     */
    OK(200,"成功", "操作成功"),

    /**
     * 服务器异常
     */
    ERROR(500,"服务器内部错误", "服务器繁忙，请稍后再试"),

    /**
     * 数据库异常
     */
    DATA_EXISTS(500, "数据库主键冲突","数据已存在，请检查"),

    /**
     * 未授权
     */
    UNAUTHORIZED(401, "未授权", "无权限访问"),

    /**
     * 404异常
     */
    NOT_FOUND(404, "接口不存在", "网页或文件未找到"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400,"非法参数！", "参数错误"),

    /**
     * 拒绝访问
     */
    FORBIDDEN(403,"拒绝访问！", "无权限访问");

    private int code;
    private String msg;
    private String text;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.text = msg;
    }

    ResultEnum(int code, String msg, String text) {
        this.code = code;
        this.msg = msg;
        this.text = text;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String getText() {
        return text;
    }

}
