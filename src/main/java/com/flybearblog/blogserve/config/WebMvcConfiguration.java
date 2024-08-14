package com.flybearblog.blogserve.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配请求
                .allowedOriginPatterns("*") // 配置来源，例如：http://localhost:19000
                .allowedMethods("*") // 配置请求方式
                .allowedHeaders("*") // 配置请求头
                .allowCredentials(true) // 允许使用凭证
                .maxAge(3600);
    }

}
