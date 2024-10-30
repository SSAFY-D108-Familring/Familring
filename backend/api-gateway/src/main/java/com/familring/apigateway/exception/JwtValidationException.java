package com.familring.apigateway.exception;

import org.springframework.http.HttpStatus;

public class JwtValidationException extends RuntimeException {
    private final HttpStatus status;

    public JwtValidationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}