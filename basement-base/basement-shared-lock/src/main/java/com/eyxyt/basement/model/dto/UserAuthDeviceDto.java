package com.eyxyt.basement.model.dto;

import com.eyxyt.basement.annotation.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author cd.wang
 * @create 2020-08-19 10:38
 * @since 1.0.0
 */
@Data
@ApiModel
public class UserAuthDeviceDto {

    /**
     * app用户id
     * 修改组 这个字段不能为空 添加可以为空
     */
    @ApiModelProperty(name = "platformUserId",value = "平台用户ID", required = true)
    @NotNull(message = "用户ID不能为空！", groups = {Update.class})
    private Long platformUserId;
}
