package com.familring.common_service.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.timeStamp = LocalDateTime.now().withNano(0);
    }
}