package com.familring.apigateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtValidationException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    public JwtValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.httpStatus = errorCode.getHttpStatus();
    }

}