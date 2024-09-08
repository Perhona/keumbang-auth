package com.keumbang.api.auth_server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(BAD_REQUEST, "유효하지 않은 입력 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "시스템 오류가 발생했습니다."),

    // 인증&인가
    AUTHENTICATION_FAILED(UNAUTHORIZED, "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTHORIZATION_HEADER_MISSING(UNAUTHORIZED, "Authorization 헤더값이 유효하지 않습니다."),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰 인증 시간이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "토큰 형식이 유효하지 않습니다."),
    TOKEN_TYPE_NOT_MATCHED(UNAUTHORIZED, "토큰 타입이 유효하지 않습니다."),
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "refresh 토큰이 만료되었거나 유효하지 않습니다. 다시 로그인해주세요."),
    REFRESH_TOKEN_NOT_FOUND(UNAUTHORIZED, "refresh 토큰을 찾을 수 없습니다."),
    ACCESS_TOKEN_HEADER_MISSING(UNAUTHORIZED, "access 토큰 헤더값이 유효하지 않습니다."),

    // 사용자,
    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    ACCOUNT_ALREADY_REGISTERED(HttpStatus.CONFLICT, "이미 등록된 계정입니다."),
    PASSWORD_NOT_MATCHED(BAD_REQUEST, "비밀번호가 일치하지 않습니다.")

    ;



    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
