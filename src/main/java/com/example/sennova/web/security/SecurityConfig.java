package com.example.sennova.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_SUPERADMIN = "SUPERADMIN";
    private final String ROLE_ANALYST = "ANALYST";
    private final JwtFilter jwtFilter;

    @Autowired
    private CorsConfig corsConfig;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.
                csrf(AbstractHttpConfigurer::disable).
                cors(httpSecurityCorsConfigurer -> {
                    httpSecurityCorsConfigurer.configurationSource(corsConfig.corsConfigurationSource());
                }).
                authorizeHttpRequests(request -> {
                            // Auth request
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/signIn").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh/token").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/logout").permitAll();
                            request.requestMatchers(HttpMethod.POST, "/api/v1/auth/signIn/google").permitAll();

                            // users
                            request.requestMatchers(HttpMethod.POST, "/api/v1/users/save").hasRole(ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getAll").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN);
                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getByName/**").hasAnyRole(ROLE_ADMIN, ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getByDni/**").hasAnyRole(ROLE_ADMIN, ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.GET, "/api/v1/users/getByRole/**").hasAnyRole(ROLE_ADMIN, ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.PUT, "/api/v1/users/update/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);
                            request.requestMatchers(HttpMethod.DELETE, "/api/v1/users/delete/**").hasAnyRole(ROLE_SUPERADMIN);

                            // products

                            request.requestMatchers(HttpMethod.POST, "/api/v1/product/**").hasRole(ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.PUT, "/api/v1/product/**").hasRole(ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.GET, "/api/v1/product/**").hasAnyRole(ROLE_SUPERADMIN, ROLE_ADMIN, ROLE_ANALYST);


                            request.requestMatchers(HttpMethod.POST, "/api/v1/location").hasRole(ROLE_SUPERADMIN);
                            request.requestMatchers(HttpMethod.POST, "/api/v1/usage/equipment").hasRole(ROLE_SUPERADMIN);


                            request.requestMatchers(HttpMethod.POST, "/api/v1/equipment/save/**").hasRole(ROLE_SUPERADMIN);


                            // Usages and location


                            request.anyRequest().authenticated();
                        }
                )
                .headers(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).
                build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
