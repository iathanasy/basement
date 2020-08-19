package com.eyxyt.basement.config;

import com.eyxyt.basement.interceptor.JwtAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cd.wang
 * @create 2020-07-16 9:56
 * @since 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    JwtAuthInterceptor jwtAuthInterceptor;

    List<String> excludeUrl = new ArrayList<String>();
    {
        //Swagger2的所有请求都不需要拦截
        excludeUrl.add("/swagger-ui.html");
        excludeUrl.add("/doc.html");
        excludeUrl.add("/v2/api-docs");
        excludeUrl.add("/swagger/**");
        excludeUrl.add("/swagger-resources/**");
        excludeUrl.add("/webjars/**");
        excludeUrl.add("/favicon.ico");
        excludeUrl.add("/captcha.jpg");
        excludeUrl.add("/csrf");


        excludeUrl.add("/druid/**");
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        //拦截路径可自行配置多个 可用 ，分隔开
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns("/**")

        .excludePathPatterns(excludeUrl);
    }


    /**
     * 配置 swagger-ui 映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry//.addResourceHandler("swagger-ui.html")
                .addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
