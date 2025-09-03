package com.example.sennova.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.sennova.application.usecases.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private final String SECRET_KEY = "juan242h42gh4hhfv242422bbbb2222";


    public String generateSingleAccessToken(String username, String roleAndAuthorities ){
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        String accessToken =  JWT.create()
                .withSubject(username)
                .withIssuer("sennova-backend-application")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000))
                .withClaim("authorities", roleAndAuthorities)
                .sign(algorithm);

        return accessToken;
    }

    public Map<String, String> createJwt(String username, String roleAndAuthorities ){
        Map<String, String> tokens = new HashMap<>();
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
       String refreshToken =  JWT.create()
                .withSubject(username)
                .withIssuer("sennova-backend-application")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000))
                .sign(algorithm);

       String accessToken =  JWT.create()
                .withSubject(username)
                .withIssuer("sennova-backend-application")
                .withIssuedAt(new Date())
               .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 1000))
               .withClaim("authorities", roleAndAuthorities)
                .sign(algorithm);


       tokens.put("access-token", accessToken);
       tokens.put("refresh-token", refreshToken);
       return tokens;
    }

    public Boolean validateJwt(String jwt){
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    };

    public String getUsername(String jwt){
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.require(algorithm).build().verify(jwt).getSubject();
    }

}
