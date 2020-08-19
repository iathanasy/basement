package com.eyxyt.basement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统配置信息表
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@ApiModel
@Data
@TableName("sys_config")
public class SysConfigEntity extends BaseEntity {

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(name = "id",value = "")
	private Long id;
	/**
	 * key
	 */
	@ApiModelProperty(name = "paramKey",value = "key")
	private String paramKey;
	/**
	 * value
	 */
	@ApiModelProperty(name = "paramValue",value = "value")
	private String paramValue;
	/**
	 * 状态   0：隐藏   1：显示
	 */
	@ApiModelProperty(name = "status",value = "状态   0：隐藏   1：显示")
	private Integer status;
	/**
	 * 备注
	 */
	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;

}
