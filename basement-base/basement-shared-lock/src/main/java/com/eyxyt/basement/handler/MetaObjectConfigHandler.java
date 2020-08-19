package com.eyxyt.basement.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.eyxyt.basement.entity.biz.BizAppUserEntity;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatis-plus 公共字段填充
 * 参考：https://mybatis.plus/guide/auto-fill-metainfo.html
 * @author cd.wang
 * @create 2020-07-17 16:07
 * @since 1.0.0
 */
@Component
public class MetaObjectConfigHandler implements MetaObjectHandler {

    public BizAppUserEntity getUser(){

        BizAppUserEntity user = new BizAppUserEntity();
        user.setUserName("admin");
        return user;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createBy", String.class,  getUser().getUserName());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", String.class,  getUser().getUserName());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 起始版本 3.3.0(推荐使用)
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", String.class,  getUser().getUserName());
    }
}
