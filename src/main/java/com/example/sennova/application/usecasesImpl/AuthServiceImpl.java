package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserPreferenceResponse;
import com.example.sennova.application.dto.authDto.LoginRequestDto;
import com.example.sennova.application.dto.authDto.LoginResponseDto;
import com.example.sennova.application.usecases.AuthUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.web.security.GoogleAuthService;
import com.example.sennova.web.security.JwtUtils;
import com.example.sennova.web.security.UserServiceSecurity;
import com.example.sennova.web.security.UserSystemUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl {
    private final UserUseCase userUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserServiceSecurity userServiceSecurity;
    private final GoogleAuthService googleAuthService;

    @Autowired
    public AuthServiceImpl(UserUseCase userUseCase, AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserServiceSecurity userServiceSecurity, GoogleAuthService googleAuthService) {
        this.userUseCase = userUseCase;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userServiceSecurity = userServiceSecurity;
        this.googleAuthService = googleAuthService;
    }


    public Map<String, Object> login(LoginRequestDto loginRequestDto) {

        try {
            Authentication authentication = this.authentication(loginRequestDto.username(), loginRequestDto.password());
            UserSystemUserDetails user = (UserSystemUserDetails) authentication.getPrincipal();
            String authority = user.getAuthorities().iterator().next().getAuthority();
            HashMap<String, String> jwt = (HashMap<String, String>) this.jwtUtils.createJwt(user.getUsername(), authority);

            UserModel userModel = this.userUseCase.findByUsername(user.getUsername());
            UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());
            LoginResponseDto response = new LoginResponseDto(jwt.get("access-token"), userModel.getUserId(), true, "Logged success", userModel.getPosition(), userModel.getImageProfile(), LocalDate.now(), authority, true, userModel.getUsername(), userModel.getName(), userPreferenceResponse);
            this.userUseCase.saveRefreshToken(jwt.get("refresh-token"), user.getUsername());

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("refreshToken", jwt.get("refresh-token"));
            objectMap.put("response", response);
            return objectMap;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public Map<String, Object> signInWithGoogle(Map<String, String> body) {
        String idToken = body.get("token");

        var payload = googleAuthService.verifyToken(idToken);

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        UserModel userModel = this.userUseCase.getByEmail(email);
        String authority = "ROLE_" + userModel.getRole().getNameRole();

        HashMap<String, String> jwt = (HashMap<String, String>) this.jwtUtils.createJwt(userModel.getUsername(), authority);
        UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());
        LoginResponseDto response = new LoginResponseDto(jwt.get("access-token"), userModel.getUserId(), true, "Logged success", userModel.getPosition(), userModel.getImageProfile(), LocalDate.now(), authority, true, userModel.getUsername(), userModel.getName(), userPreferenceResponse);

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("response", response);

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", jwt.get("refresh-token"))
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh/token")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        objectMap.put("cookie", refreshCookie);
        this.userUseCase.saveRefreshToken(jwt.get("refresh-token"), userModel.getUsername());
        return objectMap;
    }

    public void logout(String username) {
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

    public Map<String, Object> refreshToken(String refreshToken) {

        if (!this.jwtUtils.validateJwt(refreshToken)) {
            throw new RuntimeException();
        }
        UserDetails user = this.userServiceSecurity.loadUserByUsername(this.jwtUtils.getUsername(refreshToken));
        String authority = String.valueOf(user.getAuthorities().stream().iterator().next());
        String jwt = jwtUtils.generateSingleAccessToken(user.getUsername(), user.getAuthorities().iterator().next().getAuthority());
        UserModel userModel = this.userUseCase.findByUsername(user.getUsername());


        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", userModel.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh/token")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        UserPreferenceResponse userPreferenceResponse = new UserPreferenceResponse(userModel.isNotifyEquipment(), userModel.isNotifyReagents(), userModel.isNotifyQuotes(), userModel.isNotifyResults());


        Map<String, Object> objectMapResponse = new HashMap<>();
        objectMapResponse.put("response", new LoginResponseDto(jwt, userModel.getUserId(), true, "Logged success", userModel.getPosition(), userModel.getImageProfile(), LocalDate.now(), authority, true, userModel.getUsername(), userModel.getName(), userPreferenceResponse));
        objectMapResponse.put("refreshToken", refreshCookie);

        return objectMapResponse;
    }
}
