package com.eyxyt.basement;

import com.eyxyt.basement.exception.CustomException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cd.wang
 * @create 2020-07-22 15:57
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ShareLockerDeviceTest {

    @Autowired
    private BizDeviceInfoService deviceInfoService;

    @Autowired
    private ShareLockerDeviceService shareLockerDeviceService;


    @Test
    public void insertShareLockerDevice(){

        String deviceCode = "863412042811706";

        //
        ShareLockerDeviceEntity lockerDeviceEntity = new ShareLockerDeviceEntity();
        lockerDeviceEntity.setDeviceCode(deviceCode);
        lockerDeviceEntity.setDeviceNum("YXYT01");
        lockerDeviceEntity.setDeviceName("共享柜1号");
        lockerDeviceEntity.setAddress("成都高新区");
        lockerDeviceEntity.setDeviceDesc("安装在地下车库");
        lockerDeviceEntity.setContactPhone("18512815891");

        BizDeviceInfoEntity deviceInfoEntity = new BizDeviceInfoEntity();
        deviceInfoEntity.setDeviceCode(deviceCode);
        deviceInfoEntity.setDeviceType(3);

        BizDeviceInfoEntity deviceInfoEntity1 = deviceInfoService.selectByDeviceCode(deviceInfoEntity);
        System.out.println(deviceInfoEntity1);
        if (deviceInfoEntity1 == null) throw new CustomException("设备信息不存在！");
        shareLockerDeviceService.save(lockerDeviceEntity);

    }
}
