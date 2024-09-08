package com.keumbang.api.auth_server.auth.service;

import com.keumbang.api.auth_server.auth.entity.RefreshToken;
import com.keumbang.api.auth_server.auth.repository.TokenRepository;
import com.keumbang.api.auth_server.auth.type.TokenType;
import com.keumbang.api.auth_server.common.util.TokenManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenManager tokenManager;

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        tokenRepository.save(
                RefreshToken.builder()
                        .userId(userId)
                        .refreshToken(refreshToken)
                        .build()
        );
    }

    public String findRefreshTokenByUserId(Long userId) {
        return tokenRepository.findByUserId(userId).getRefreshToken();
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public void issueTokens(HttpServletResponse response, String username, Long userId) {
        // JWT 생성
        String accessToken = tokenManager.createToken(TokenType.ACCESS, username, userId);
        String refreshToken = tokenManager.createToken(TokenType.REFRESH, username, userId);

        // DB에 Refresh Token 저장
        saveRefreshToken(userId, refreshToken);

        // 응답에 토큰 추가
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createRefreshTokenCookie(refreshToken));

        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createRefreshTokenCookie(String value) {
        Cookie cookie = new Cookie(TokenType.REFRESH.getName(), value);
        cookie.setMaxAge(TokenType.REFRESH.getExpirationHour() * 60 * 60); // 7일간 저장
        cookie.setHttpOnly(true);
        return cookie;
    }
}
