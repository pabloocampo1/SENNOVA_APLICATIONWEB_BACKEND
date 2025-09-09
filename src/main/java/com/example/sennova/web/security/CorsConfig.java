package com.example.sennova.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource (){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("POST", "PUT", "DELETE", "GET"));
        configuration.addAllowedHeader("*");
        configuration.setAllowedOriginPatterns(List.of("http://localhost:5173"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

        return urlBasedCorsConfigurationSource;

    }
}
