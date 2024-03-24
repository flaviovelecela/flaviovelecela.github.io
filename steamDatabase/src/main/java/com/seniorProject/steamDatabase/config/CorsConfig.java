package com.seniorProject.steamDatabase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // You can restrict paths with specific ones instead of /**
                        .allowedOrigins("http://localhost:3000", "https://www.upstreamreact.com", "https://75bc-75-102-132-166.ngrok-free.app") // The origin your React app is served from
                        .allowedMethods("GET", "POST", "PUT", "DELETE") // Whatever methods you need
                        .allowCredentials(true)
                        .allowedHeaders("*")
                        .maxAge(3600); // 1 hour max age
            }
        };
    }
}
