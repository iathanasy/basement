package com.eyxyt.basement.model.dto;

import com.eyxyt.basement.bean.QueryParams;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cd.wang
 * @create 2020-08-19 10:45
 * @since 1.0.0
 */
@ApiModel
@Data
public class UserListDto extends QueryParams {

    /**
     * 用户类型, 1:宜行宜停用户
     */
    @ApiModelProperty(name = "type",value = "用户类型, 1:宜行宜停用户")
    private Long type;
}
