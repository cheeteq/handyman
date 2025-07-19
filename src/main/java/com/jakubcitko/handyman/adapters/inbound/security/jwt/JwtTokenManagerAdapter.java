package com.jakubcitko.handyman.adapters.inbound.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jakubcitko.handyman.core.application.port.out.TokenManagerPort;
import com.jakubcitko.handyman.core.domain.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokenManagerAdapter implements TokenManagerPort {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    @Override
    public String generateToken(UUID id, String email, Set<Role> roles) {
        String[] rolesArray = roles.stream()
                .map(Role::name)
                .toArray(String[]::new);

        return JWT.create()
                .withSubject(email)
                .withClaim("id", id.toString())
                .withArrayClaim("roles", rolesArray)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs))
                .sign(Algorithm.HMAC512(jwtSecret));
    }

    @Override
    public String getEmailFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(jwtSecret))
                .build()
                .verify(token)
                .getSubject();
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }
}
