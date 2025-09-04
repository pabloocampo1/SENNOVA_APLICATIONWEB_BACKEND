package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.authDto.LoginRequestDto;
import com.example.sennova.application.dto.authDto.LoginResponseDto;
import com.example.sennova.application.usecases.AuthUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.web.security.JwtUtils;
import com.example.sennova.web.security.UserServiceSecurity;
import com.example.sennova.web.security.UserSystemUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

@Service
public class AuthServiceImpl {
    private final UserUseCase userUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceSecurity userServiceSecurity;

    @Autowired
    public AuthServiceImpl(UserUseCase userUseCase, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserServiceSecurity userServiceSecurity) {
        this.userUseCase = userUseCase;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userServiceSecurity = userServiceSecurity;
    }


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = this.authentication(loginRequestDto.username(), loginRequestDto.password());
            UserSystemUserDetails user = (UserSystemUserDetails) authentication.getPrincipal();
            String authority = user.getAuthorities().iterator().next().getAuthority();
            HashMap<String, String> jwt = (HashMap<String, String>) this.jwtUtils.createJwt(user.getUsername(), authority);

           LoginResponseDto response = new  LoginResponseDto(jwt.get("access-token"), jwt.get("refresh-token"), 22L, true, "Logged success", LocalDate.now(), authority);
            this.userUseCase.saveRefreshToken( response.refreshToken(), user.getUsername());
            return  response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void logout(String username){
        this.userUseCase.deleteRefreshToken(username);
    }

    // method for validate with spring security if the username and password are correct
    public Authentication authentication(@Valid String username, @Valid String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                password
        );
        return this.authenticationManager.authenticate(authenticationToken);
    }

    public String refreshToken(String refreshToken){

        if (!this.jwtUtils.validateJwt(refreshToken)){
            throw new RuntimeException();
        }

        UserDetails user = this.userServiceSecurity.loadUserByUsername(this.jwtUtils.getUsername(refreshToken));
        return this.jwtUtils.generateSingleAccessToken(user.getUsername(), user.getAuthorities().iterator().next().getAuthority());
    }
}
