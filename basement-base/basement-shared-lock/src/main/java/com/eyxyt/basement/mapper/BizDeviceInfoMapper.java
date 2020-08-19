package com.eyxyt.basement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 设备信息表
 * 
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-22 10:20:45
 */
public interface BizDeviceInfoMapper extends BaseMapper<BizDeviceInfoEntity> {

    /**
     * 根据设备Code 查询
     * @param deviceCode
     * @param deviceType
     * @return
     */
    BizDeviceInfoEntity selectDeviceInfoByDeviceCode(@Param("deviceCode") String deviceCode, @Param("deviceType") Integer deviceType);
}
