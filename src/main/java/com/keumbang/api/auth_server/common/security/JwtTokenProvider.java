package com.keumbang.api.auth_server.common.security;

import com.keumbang.api.auth_server.auth.type.TokenType;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String createJwt(TokenType tokenType, String username, Long userId) {
        return Jwts.builder()
                .claim("category", tokenType.getName())
                .claim("account", username)
                .claim("userId", userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ((long) tokenType.getExpirationHour() * 60 * 60 * 1000)))
                .signWith(secretKey)
                .compact();
    }
}
