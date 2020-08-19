package com.eyxyt.basement;

import com.eyxyt.basement.entity.biz.BizAppUserEntity;
import com.eyxyt.basement.service.BizAppUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cd.wang
 * @create 2020-07-17 17:14
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private BizAppUserService userService;

    @Test
    public void insertUser(){
        BizAppUserEntity userEntity = new BizAppUserEntity();
//        userEntity.setId("1284061035111452674");
        userEntity.setPlatformUserId("1510");
        userEntity.setMobile("15008400952");
        userEntity.setNickName("半仙123");
        userEntity.setUserName("banxian");

//        userService.save(userEntity);
        userService.saveOrUpdate(userEntity);
    }
}
