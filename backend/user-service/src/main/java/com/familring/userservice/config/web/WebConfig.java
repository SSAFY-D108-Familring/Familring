package com.familring.userservice.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${familring.server.url}")
    private String familringServerUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(familringServerUrl, "http://localhost:8000")
                .exposedHeaders("*")
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}