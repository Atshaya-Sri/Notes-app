package com.atshu.notesapp_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply this rule to all your API endpoints
                .allowedOrigins("https://notes-app-2-apin.onrender.com/") // This allows requests from ANY origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}