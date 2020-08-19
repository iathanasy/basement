package com.eyxyt.basement.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分页查询参数
 * @author cd.wang
 * @create 2020-07-17 15:49
 * @since 1.0.0
 */
@ApiModel
@Data
public class QueryParams {

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "currPage",value = "当前页码")
    private long currPage = 1;
    /**
     * 每页条数
     */
    @ApiModelProperty(name = "pageSize",value = "每页条数")
    private long pageSize = 10;

    /** 排序列 */
    @ApiModelProperty(name = "orderByColumn",value = "排序列")
    private String orderByColumn;

    /** 排序的方向 "desc" 或者 "asc". */
    @ApiModelProperty(name = "isAsc",value = "排序的方向（desc 或 asc）")
    private String isAsc = "asc";

    /**
     * 搜索
     */
    @ApiModelProperty(name = "key",value = "搜索")
    private String key;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "beginTime",value = "开始时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date beginTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime",value = "结束时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endTime;


    @ApiModelProperty(name = "createBy",value = "创建人")
    private String createBy;
}
