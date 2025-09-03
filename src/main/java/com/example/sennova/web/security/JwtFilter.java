package com.example.sennova.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserServiceSecurity userServiceSecurity;

    @Autowired
    public JwtFilter(JwtUtils jwtUtils, UserServiceSecurity userServiceSecurity) {
        this.jwtUtils = jwtUtils;
        this.userServiceSecurity = userServiceSecurity;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);


       if(!this.jwtUtils.validateJwt(jwt)){
           filterChain.doFilter(request, response);
           return;
       }


       String username = this.jwtUtils.getUsername(jwt);

      try{
          UserDetails user = this.userServiceSecurity.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                  user.getUsername(),
                  user.getPassword(),
                  user.getAuthorities()
          );

          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

      } catch (Exception e) {
          System.out.println("fue aca bro");
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

          return;
      }

      filterChain.doFilter(request, response);

    }
}
