package com.eyxyt.basement.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 角色与菜单对应关系
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
@ApiModel
@Data
@TableName("sys_role_menu")
public class SysRoleMenuEntity extends BaseEntity {

	/**
	 * 
	 */
	@TableId
	@ApiModelProperty(name = "id",value = "")
	private Long id;
	/**
	 * 角色ID
	 */
	@ApiModelProperty(name = "roleId",value = "角色ID")
	private Long roleId;
	/**
	 * 菜单ID
	 */
	@ApiModelProperty(name = "menuId",value = "菜单ID")
	private Long menuId;

}
