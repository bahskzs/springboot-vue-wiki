package com.yqy.wiki.config;


import com.yqy.wiki.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author bahsk
 * @createTime 2021-06-24 20:06
 * @description
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/redis/**",
                        "/user/login",
                        "/doc/all/**",
                        "/category/all",
                        "/doc/find-content/**",
                        "/ebook/list",
                        "/category/**",
                        "/doc/**"
                );
    }
}
