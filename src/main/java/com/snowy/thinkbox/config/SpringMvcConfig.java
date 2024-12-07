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
    @Bean
    public HandlerInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login",
                        "/user/register",
                        "category/list",
                        "/ebook/list",
                        "/doc/list",
                        "/doc/find-content",
                        "/doc/all",
                        "/doc/content",
                        "/category/all",
                        "/user/logout"
                );

    }
}
