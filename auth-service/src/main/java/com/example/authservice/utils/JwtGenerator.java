package com.example.authservice.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.authservice.config.JwtConfig;
import com.example.authservice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtGenerator {

    private static final long JWT_TTL_MILLIS = 600000;

    @Autowired
    private JwtConfig config;

    public String generateToken(User user) {
        try {
            long nowMillis = System.currentTimeMillis();
            return JWT.create()
                    .withSubject(user.getName())
                    .withClaim("email", user.getEmail())
                    .withClaim("role", user.getRole().toString())
                    .withIssuedAt(new Date(nowMillis))
                    .withExpiresAt(new Date(nowMillis + JWT_TTL_MILLIS))
                    .sign(Algorithm.HMAC256(config.getSecret()));
        } catch (JWTCreationException exception) {
            throw new RuntimeException("You need to enable Algorithm.HMAC256");
        }
    }


}
