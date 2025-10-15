package com.project.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig{

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // allowing json to be handled with js (Ajax, Axios, jQueryAjax). if false, the response is blocked
        config.setAllowedOrigins(List.of(
                "http://127.0.0.1:5500",
                "http://localhost:5500"
        ));
        config.addAllowedHeader("*"); // allowing responses to all the headers
        config.addAllowedMethod("*"); // allowing responsese to all the methods; post, put, get, delete, patch
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}