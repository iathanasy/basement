package com.eyxyt.basement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author cd.wang
 * @create 2020-07-23 11:45
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.eyxyt.basement.mapper")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
