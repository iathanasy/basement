package com.eyxyt.basement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码生成启动
 */
@SpringBootApplication
@MapperScan("com.eyxyt.basement.dao")
public class BasementGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasementGeneratorApplication.class, args);
	}
}
