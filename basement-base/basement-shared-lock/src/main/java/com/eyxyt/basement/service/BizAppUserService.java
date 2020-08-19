package com.eyxyt.basement.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.biz.BizAppUserEntity;

/**
 * 用户信息表
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-17 10:53:59
 */
public interface BizAppUserService extends IService<BizAppUserEntity> {

    BizAppUserEntity selectOne(String mobile);

    PageVo queryPage(Query params);

    PageVo selectPage(Query params);
}

