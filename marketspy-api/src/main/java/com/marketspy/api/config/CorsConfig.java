package com.marketspy.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // libera todas as rotas da API
                .allowedOrigins("http://localhost:5173", "http://localhost:5174") // libera as portas do vite
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // libera os verbos HTTP
    }
    
}
