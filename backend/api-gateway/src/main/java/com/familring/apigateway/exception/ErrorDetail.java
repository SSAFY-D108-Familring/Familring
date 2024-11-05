package com.familring.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorDetail {
    EMPTY_AUTHORIZATION_HEADER("T0001", HttpStatus.UNAUTHORIZED, "인증 헤더가 존재하지 않습니다."),
    EMPTY_SUBJECT("T0002", HttpStatus.UNAUTHORIZED, "JWT 토큰의 사용자 정보가 존재하지 않습니다."),
    EMPTY_TOKEN("T0003", HttpStatus.UNAUTHORIZED, "JWT 토큰의 클레임이 존재하지 않습니다."),
    EXPIRED_TOKEN("T0004", HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다."),
    INVALID_SIGNATURE("T0005", HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다."),
    UNSUPPORTED_TOKEN("T0006", HttpStatus.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다."),
    INVALID_TOKEN("T0007", HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 토큰입니다.");

    private String errorCode;
    private HttpStatus httpStatus;
    private String errorMessage;
}

