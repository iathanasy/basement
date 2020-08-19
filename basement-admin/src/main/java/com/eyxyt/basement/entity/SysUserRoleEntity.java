package com.eyxyt.basement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户与角色对应关系
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@ApiModel
@Data
@TableName("sys_user_role")
public class SysUserRoleEntity extends BaseEntity {

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(name = "id",value = "")
	private Long id;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(name = "userId",value = "用户ID")
	private Long userId;
	/**
	 * 角色ID
	 */
	@ApiModelProperty(name = "roleId",value = "角色ID")
	private Long roleId;

}
