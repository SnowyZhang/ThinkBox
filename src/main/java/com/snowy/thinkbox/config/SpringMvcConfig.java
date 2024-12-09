package com.snowy.thinkbox.config;

import com.snowy.thinkbox.interceptor.LoginInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login",
                        "/user/register",
                        "category/list",
                        "/redis/**",
                        "/ebook/list",
                        "/doc?ebookId=**",
                        "/doc/list",
                        "/doc/all/**",
                        "/doc/content/**",
                        "/category/all",
                        "/user/logout/**",
                        "/doc/vote/**"

                );

    }
}
