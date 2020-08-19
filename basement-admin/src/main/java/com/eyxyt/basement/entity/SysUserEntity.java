package com.eyxyt.basement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 系统用户
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@ApiModel
@Data
@TableName("sys_user")
public class SysUserEntity extends BaseEntity {

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(name = "id",value = "")
	private Long id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(name = "username",value = "用户名")
	private String username;
	/**
	 * 密码
	 */
	@ApiModelProperty(name = "password",value = "密码")
	private String password;
	/**
	 * 盐
	 */
	@ApiModelProperty(name = "salt",value = "盐")
	private String salt;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(name = "email",value = "邮箱")
	private String email;
	/**
	 * 手机号
	 */
	@ApiModelProperty(name = "mobile",value = "手机号")
	private String mobile;
	/**
	 * 状态  0：禁用   1：正常
	 */
	@ApiModelProperty(name = "status",value = "状态  0：禁用   1：正常")
	private Integer status;


	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;
}
