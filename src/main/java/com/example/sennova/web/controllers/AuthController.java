package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.authDto.LoginRequestDto;
import com.example.sennova.application.dto.authDto.LoginResponseDto;
import com.example.sennova.application.usecasesImpl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        try {
            Map<String, Object> response = this.authService.login(loginRequestDto);

            Object loginResponseDto = response.get("response");
            System.out.println(loginResponseDto);

            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", response.get("refreshToken").toString())
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/v1/auth/refresh/token")
                    .maxAge(Duration.ofDays(7))
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(loginResponseDto);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales incorrectas");
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Usuario no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Credenciales inválidas");
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam("username") @Valid String username) {
       try {
           this.authService.logout(username);
           return new ResponseEntity<>(HttpStatus.OK);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
    }

    @PostMapping("/refresh/token")
    public ResponseEntity<Object> login(@CookieValue("refreshToken") String refreshToken) {
        Map<String, Object> objectMap = this.authService.refreshToken(refreshToken);
        Object loginResponseDto = objectMap.get("response");
        Object cookie = objectMap.get("refreshToken");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDto);
    }

    ;
}
