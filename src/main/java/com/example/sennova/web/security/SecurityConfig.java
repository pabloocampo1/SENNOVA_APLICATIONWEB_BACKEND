package com.example.sennova.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_SUPERADMIN = "SUPERADMIN";
    private final String ROLE_ANALYST = "ANALYST";
    private final JwtFilter jwtFilter;

    @Autowired
    private  CorsConfig corsConfig;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http.
                csrf(AbstractHttpConfigurer::disable).
                cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource());
                }).
                authorizeHttpRequests(request -> {
                    // Auth request
                    request.requestMatchers(HttpMethod.POST, "/api/v1/auth/signIn").permitAll();
                    request.requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh/token/**").permitAll();


                    request.requestMatchers(HttpMethod.GET,"/api/v1/users/getAll").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN);
                    request.requestMatchers(HttpMethod.POST,"/api/v1/users/save").permitAll();
                    request.anyRequest().authenticated();
                        }
                ).

                addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
                build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws  Exception{
        return configuration.getAuthenticationManager();
    }



}
