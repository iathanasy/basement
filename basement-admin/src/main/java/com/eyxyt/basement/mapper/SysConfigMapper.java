package com.eyxyt.basement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eyxyt.basement.entity.SysConfigEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 系统配置信息表
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */

public interface SysConfigMapper extends BaseMapper<SysConfigEntity> {
    /**
     * 根据key，查询value
     */
    SysConfigEntity queryByKey(String paramKey);

    /**
     * 根据key，更新value
     */
    int updateValueByKey(@Param("paramKey") String paramKey, @Param("paramValue") String paramValue);
}
