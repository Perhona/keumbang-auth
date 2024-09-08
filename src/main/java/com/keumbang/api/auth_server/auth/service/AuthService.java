package com.keumbang.api.auth_server.auth.service;

import com.keumbang.api.auth_server.auth.dto.LoginRequestDto;
import com.keumbang.api.auth_server.auth.dto.SignUpRequestDto;
import com.keumbang.api.auth_server.auth.type.TokenType;
import com.keumbang.api.auth_server.common.exception.CustomException;
import com.keumbang.api.auth_server.common.exception.ErrorCode;
import com.keumbang.api.auth_server.common.util.PasswordManager;
import com.keumbang.api.auth_server.user.entity.User;
import com.keumbang.api.auth_server.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;
    private final TokenService tokenService;

    @Transactional
    public long signUp(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByAccount(signUpRequestDto.getAccount())) {
            throw new CustomException(ErrorCode.ACCOUNT_ALREADY_REGISTERED);
        }

        return userRepository.save(
                User.builder()
                        .account(signUpRequestDto.getAccount())
                        .password(passwordManager.encodePassword(signUpRequestDto.getPassword()))
                        .createdAt(LocalDateTime.now())
                        .build()
        ).getId();
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // 사용자 정보 확인
        User user = userRepository.findByAccount(loginRequestDto.getAccount()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 비밀번호 검증
        if (!passwordManager.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCHED);
        }

        // 토큰 발급
        tokenService.deleteRefreshToken(user.getId());
        tokenService.issueTokens(response, user.getAccount(), user.getId());

    }

    @Transactional
    public void reissueTokens(HttpServletRequest request, HttpServletResponse response) {
        // Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(TokenType.REFRESH.getName())).findFirst().orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND)).getValue();

        tokenService.validateRefreshToken(refreshToken);
        tokenService.reissueTokens(response, refreshToken);
    }
}
