package com.jglee.cafe.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // 요청 - 뷰 연결
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("forward:/index");
//        registry.addViewController("/index").setViewName("index");
//        registry.addViewController("/login").setViewName("login");
//        registry.addViewController("/admin").setViewName("admin");
//        registry.addViewController("/signup").setViewName("signup");
//        registry.addViewController("/tmp").setViewName("tmp");
    }
}