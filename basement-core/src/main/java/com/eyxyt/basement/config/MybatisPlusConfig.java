package com.eyxyt.basement.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置
 * @author cd.wang
 * @create 2020-07-17 11:28
 * @since 1.0.0
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     * 详细请参考：https://mybatis.plus/guide/page.html
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}