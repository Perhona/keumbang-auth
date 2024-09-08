package com.keumbang.api.auth_server.common.util;

import com.keumbang.api.auth_server.auth.type.TokenType;
import com.keumbang.api.auth_server.common.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManager {

    private final JwtTokenProvider jwtTokenProvider;

    public String createToken(TokenType tokenType, String username, Long userId){
        return jwtTokenProvider.createJwt(tokenType, username, userId);
    }

}
