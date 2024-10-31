package com.familring.apigateway.dto;

import com.familring.common_service.base.ErrorResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorCodeResponse {
    private String code;
    private String message;
    private LocalDateTime timeStamp;

    public ErrorCodeResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.timeStamp = LocalDateTime.now().withNano(0);
    }
}