package com.keumbang.api.auth_server.auth.service;

import com.keumbang.api.auth_server.auth.dto.SignUpRequestDto;
import com.keumbang.api.auth_server.common.exception.CustomException;
import com.keumbang.api.auth_server.common.exception.ErrorCode;
import com.keumbang.api.auth_server.common.util.PasswordManager;
import com.keumbang.api.auth_server.user.entity.User;
import com.keumbang.api.auth_server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordManager passwordManager;

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

}
