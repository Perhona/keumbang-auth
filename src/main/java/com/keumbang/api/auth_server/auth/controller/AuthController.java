package com.keumbang.api.auth_server.auth.controller;

import com.keumbang.api.auth_server.auth.dto.LoginRequestDto;
import com.keumbang.api.auth_server.auth.dto.SignUpRequestDto;
import com.keumbang.api.auth_server.auth.service.AuthService;
import com.keumbang.api.auth_server.common.exception.CustomException;
import com.keumbang.api.auth_server.common.exception.JwtAuthenticationException;
import com.keumbang.api.auth_server.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 회원가입
     *
     * @param signUpRequestDto 회원가입 요청 DTO
     * @return 사용자 Id
     * @throws CustomException 계정명 중복 -> ACCOUNT_ALREADY_REGISTERED
     */
    @Operation(summary = "사용자 회원가입", description = "사용자는 계정과 비밀번호로 회원 가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Long>> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        CommonResponse<Long> response = CommonResponse.ok("회원가입에 성공했습니다.", authService.signUp(signUpRequestDto));

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    /**
     * 사용자 로그인
     *
     * @param loginRequestDto 로그인 요청 DTO
     * @return Access Token과 Refresh Token
     * @throws CustomException 인증 실패 -> USER_NOT_FOUND, PASSWORD_NOT_MATCHED
     */
    @Operation(summary = "사용자 로그인", description = "사용자는 계정과 비밀번호로 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<?>> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authService.login(loginRequestDto, response);
        return new ResponseEntity<>(CommonResponse.ok("로그인에 성공하였습니다."), HttpStatus.OK);
    }

    /**
     * Access Token 재발급, Refresh Token 갱신
     *
     * @return 200 Authorization Header AccessToken, Cookie RefreshToken 갱신
     * @throws JwtAuthenticationException RefreshToken이 유효하지 않은 경우
     */
    @Operation(summary = "Access Token 재발급, Refresh Token 갱신", description = "기존 Refresh Token으로 Access Token 재발급하고 Authorization Header에 반환합니다. Refresh Token또한 갱신하여 Cookie에 저장됩니다.")
    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<?>> reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueTokens(request, response);
        return new ResponseEntity<>(CommonResponse.ok("Access Token 재발급, Refresh Token 갱신에 성공했습니다."), HttpStatus.OK);
    }
}
