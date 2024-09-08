package com.keumbang.api.auth_server.auth.controller;

import com.keumbang.api.auth_server.auth.dto.LoginRequestDto;
import com.keumbang.api.auth_server.auth.dto.SignUpRequestDto;
import com.keumbang.api.auth_server.auth.service.AuthService;
import com.keumbang.api.auth_server.common.exception.CustomException;
import com.keumbang.api.auth_server.common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
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

}
