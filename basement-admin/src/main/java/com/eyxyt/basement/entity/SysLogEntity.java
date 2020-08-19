package com.eyxyt.basement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统日志
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@ApiModel
@Data
@TableName("sys_log")
public class SysLogEntity extends BaseEntity {

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
	 * 用户操作
	 */
	@ApiModelProperty(name = "operation",value = "用户操作")
	private String operation;
	/**
	 * 请求方法
	 */
	@ApiModelProperty(name = "method",value = "请求方法")
	private String method;
	/**
	 * 请求参数
	 */
	@ApiModelProperty(name = "params",value = "请求参数")
	private String params;
	/**
	 * 执行时长(毫秒)
	 */
	@ApiModelProperty(name = "time",value = "执行时长(毫秒)")
	private Long time;
	/**
	 * IP地址
	 */
	@ApiModelProperty(name = "ip",value = "IP地址")
	private String ip;
}
