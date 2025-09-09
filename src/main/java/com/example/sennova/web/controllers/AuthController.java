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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
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
        } catch (Exception e) {
            LoginResponseDto response = new  LoginResponseDto(null,  0L, false, "Logged not success", LocalDate.now(), null);
           return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    ;

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam("username") @Valid String username) {
        this.authService.logout(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    ;

    @PostMapping("/refresh/token")
    public ResponseEntity<String> login(@CookieValue("refreshToken") String refreshToken) {
        return new ResponseEntity<>(this.authService.refreshToken(refreshToken), HttpStatus.OK);
    }

    ;
}
