package com.example.Authentication_service.util;


import com.example.Authentication_service.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {



    @Value("${jwt.expiration:86400000}") // 24 часа
    private long expiration;

    private SecretKey getSigningKey() {
        byte[] keyBytes = "mySuperSecretKeyForJWTGenerationInAuthService2024!ChangeThisInProduction__1234".getBytes(StandardCharsets.UTF_8);
        return new javax.crypto.spec.SecretKeySpec(keyBytes, "HmacSHA256");
    }


    public String generateToken(AuthUser user) {
        return Jwts.builder()
                .subject(user.getEmployeeId())
                .claim("employeeId", user.getEmployeeId())
                .claim("userId", user.getId())
                .claim("fullName", user.getFullName())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getJwtParser().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmployeeIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }

    private Claims getClaimsFromToken(String token) {
        return getJwtParser()
                .parseSignedClaims(token)
                .getPayload();
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build();
    }
}