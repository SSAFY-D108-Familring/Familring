package com.familring.apigateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JwtValidationException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public JwtValidationException(ErrorDetail errorDetail) {
        super(errorDetail.getErrorMessage());
        this.errorCode = errorDetail.getErrorCode();
        this.httpStatus = errorDetail.getHttpStatus();
    }

}