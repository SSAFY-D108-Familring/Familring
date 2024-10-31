package com.familring.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    MISSING_AUTHORIZATION_HEADER("T0001", HttpStatus.UNAUTHORIZED, "인증 헤더가 존재하지 않습니다"),
    MISSING_SUBJECT("T0002", HttpStatus.UNAUTHORIZED, "JWT 토큰의 사용자 정보가 존재하지 않습니다"),
    MISSING_USER_ID("T0003", HttpStatus.UNAUTHORIZED, "JWT 토큰의 사용자 ID가 존재하지 않습니다"),
    EXPIRED_TOKEN("T0004", HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다"),
    INVALID_SIGNATURE("T0005", HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다"),
    UNSUPPORTED_TOKEN("T0006", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다"),
    INVALID_TOKEN("T0007", HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다");

    private String code;
    private HttpStatus httpStatus;
    private String message;
}

