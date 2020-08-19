package com.eyxyt.basement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.SysConfigEntity;


/**
 * 系统配置信息表
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysConfigService extends IService<SysConfigEntity> {

    PageVo queryPage(Query params);

    /**
     * 根据key，获取配置的value值
     *
     * @param key           key
     */
    public String getValue(String key);

    /**
     * 根据key，获取value的Object对象
     * @param key    key
     * @param clazz  Object对象
     */
    public <T> T getConfigObject(String key, Class<T> clazz);
}

