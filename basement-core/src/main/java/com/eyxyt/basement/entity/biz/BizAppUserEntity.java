package com.eyxyt.basement.entity.biz;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.eyxyt.basement.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息表
 * @author cd.wang
 * @create 2020-07-17 11:00
 * @since 1.0.0
 */
@ApiModel
@Data
@TableName("biz_app_user")
public class BizAppUserEntity extends BaseEntity {
    /**
     * 默认雪花算法生成
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(name = "id",value = "")
    private String id;
    /**
     * 平台用户ID
     */
    @ApiModelProperty(name = "platformUserId",value = "平台用户ID")
    private String platformUserId;
    /**
     * 用户手机号
     */
    @ApiModelProperty(name = "mobile",value = "用户手机号")
    private String mobile;

    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName",value = "用户名")
    private String userName;

    /**
     * 用户昵称
     */
    @ApiModelProperty(name = "nickName",value = "用户昵称")
    private String nickName;
    /**
     * 用户头像
     */
    @ApiModelProperty(name = "headImg",value = "用户头像")
    private String headImg;
    /**
     * 省份
     */
    @ApiModelProperty(name = "province",value = "省份")
    private String province;
    /**
     * 城市
     */
    @ApiModelProperty(name = "city",value = "城市")
    private String city;
    /**
     * 邮箱
     */
    @ApiModelProperty(name = "email",value = "邮箱")
    private String email;
    /**
     * 用户类型, 1:宜行宜停用户
     */
    @ApiModelProperty(name = "type",value = "用户类型, 1:宜行宜停用户")
    private Long type;
    /**
     * 密码盐值
     */
    @ApiModelProperty(name = "salt",value = "密码盐值")
    private String salt;
    /**
     * 等级
     */
    @ApiModelProperty(name = "level",value = "等级")
    private String level;
    /**
     * 用户身份
     */
    @ApiModelProperty(name = "identity",value = "用户身份")
    private String identity;
    /**
     * 性别
     */
    @ApiModelProperty(name = "gender",value = "性别")
    private String gender;
    /**
     * 注册来源
     */
    @ApiModelProperty(name = "registSource",value = "注册来源")
    private String registSource;
    /**
     * 注册时间
     */
    @ApiModelProperty(name = "registTime",value = "注册时间")
    private Date registTime;
    /**
     * 登录时间
     */
    @ApiModelProperty(name = "loginTime",value = "登录时间")
    private Date loginTime;
}
