package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.authDto.ChangePasswordRequest;
import com.example.sennova.application.dto.authDto.LoginRequestDto;
import com.example.sennova.application.usecasesImpl.AuthServiceImpl;
import com.example.sennova.infrastructure.restTemplate.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthServiceImpl authService;
    private final EmailService emailService;


    @Autowired
    public AuthController(AuthServiceImpl authService, EmailService emailService) {
        this.authService = authService;

        this.emailService = emailService;
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

    @PostMapping("/signIn/google")
    public ResponseEntity<Object> loginByGoogle(@RequestBody Map<String, String> body) {
        try {

            Map<String, Object> signInWithGoogle = this.authService.signInWithGoogle(body);

            System.out.println("response 2: " + signInWithGoogle);
            Object response = signInWithGoogle.get("response");
            Object cookie = signInWithGoogle.get("cookie");
            System.out.println("cookie : " + cookie);
            System.out.println("response 2: " + response);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, ((ResponseCookie) cookie).toString())
                    .body(response);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Credenciales incorrectas");
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("Usuario no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("Credenciales inválidas");
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam("username") @Valid String username) {
        try {
            this.authService.logout(username);
            ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)
                    .path("/api/v1/auth/refresh/token")
                    .maxAge(0)
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                    .body(HttpStatus.OK);
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

    @PostMapping("/generateToken/change/email/{currentEmail}/{newEmail}")
    public ResponseEntity<Boolean> generateTokenChangeEmail(
            @PathVariable("currentEmail") @Valid String currentEmail,
            @PathVariable("newEmail") @Valid String newEmail
    ) {
        try {
            Integer code = this.authService.generateTokenChangeEmail(currentEmail, newEmail);

            this.emailService.sendVerificationCode(newEmail, code);

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/validate/code/changeEmail/{code}")
    public ResponseEntity<String> validateTokenChangeEmail(
            @PathVariable("code") @Valid Integer code
    ) {
        try {
           String status = this.authService.validateCodeChangeEmail(code);
            return new ResponseEntity<>(status, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid ChangePasswordRequest request) {

        String username = userDetails.getUsername();

        Boolean status = this.authService.changePassword(username, request);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }


}
