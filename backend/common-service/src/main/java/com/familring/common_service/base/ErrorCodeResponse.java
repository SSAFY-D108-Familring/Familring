package com.familring.common_service.base;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorCodeResponse extends ErrorResponse {
    private String code;

    public ErrorCodeResponse(String code, String message) {
        super(message);
        this.code = code;
    }
}