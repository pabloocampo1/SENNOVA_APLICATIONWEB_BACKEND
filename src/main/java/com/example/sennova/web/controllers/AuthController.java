package com.example.sennova.web.controllers;

import com.example.sennova.application.dto.authDto.LoginRequestDto;
import com.example.sennova.application.dto.authDto.LoginResponseDto;
import com.example.sennova.application.usecasesImpl.AuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthServiceImpl authService;

    @Autowired
    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto){

        return new ResponseEntity<>(this.authService.login(loginRequestDto), HttpStatus.OK);
    };

    @PostMapping("/refresh/token/{token}")
    public ResponseEntity<String> login(@PathVariable("token") @Valid String refreshToken){
        return new ResponseEntity<>(this.authService.refreshToken(refreshToken), HttpStatus.OK);
    };
}
