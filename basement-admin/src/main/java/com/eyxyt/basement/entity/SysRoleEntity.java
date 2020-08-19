package com.eyxyt.basement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@ApiModel
@Data
@TableName("sys_role")
public class SysRoleEntity extends BaseEntity {

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(name = "id",value = "")
	private Long id;
	/**
	 * 角色名称
	 */
	@ApiModelProperty(name = "roleName",value = "角色名称")
	private String roleName;
	/**
	 * 备注
	 */
	@ApiModelProperty(name = "remark",value = "备注")
	private String remark;

	@TableField(exist=false)
	private List<Long> menuIdList;
}
