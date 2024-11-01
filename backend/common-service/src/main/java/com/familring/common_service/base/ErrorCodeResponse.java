package com.familring.common_service.base;

import lombok.Getter;

@Getter
public class ErrorCodeResponse extends ErrorResponse {
    private String errorCode;

    public ErrorCodeResponse(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}