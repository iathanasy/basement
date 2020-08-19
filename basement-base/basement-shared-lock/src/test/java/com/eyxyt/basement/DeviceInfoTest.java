package com.eyxyt.basement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cd.wang
 * @create 2020-07-22 11:21
 * @since 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceInfoTest {

    @Autowired
    private BizDeviceInfoService deviceInfoService;


    /**
     * 添加设备信息
     */
    @Test
    public void insertDeviceInfo(){

        BizDeviceInfoEntity deviceInfoEntity = new BizDeviceInfoEntity();
        deviceInfoEntity.setDeviceCode("863412042811706");
        deviceInfoEntity.setDeviceType(3);

        BizDeviceInfoEntity deviceInfoEntity1 = deviceInfoService.selectByDeviceCode(deviceInfoEntity);
        if(deviceInfoEntity1 == null) {
            deviceInfoService.save(deviceInfoEntity);
        }else{
            deviceInfoService.saveOrUpdate(deviceInfoEntity1);
        }
    }

}
