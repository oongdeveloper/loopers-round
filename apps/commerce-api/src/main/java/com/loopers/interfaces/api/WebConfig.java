package com.loopers.interfaces.api;

import com.loopers.interfaces.api.intercepter.HeaderValidationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderValidationInterceptor())
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns(
                        "/api/v1/users",
                        "/api/v1/payment/**"
                );
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
