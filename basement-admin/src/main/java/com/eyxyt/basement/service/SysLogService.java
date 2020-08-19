package com.eyxyt.basement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.SysLogEntity;


/**
 * 系统日志
 *
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-23 14:23:35
 */
public interface SysLogService extends IService<SysLogEntity> {


    PageVo queryPage(Query params);
}

