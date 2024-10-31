package com.familring.common_service.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private String message;
    private LocalDateTime timeStamp;

    public ErrorResponse(String message) {
        this.message = message;
        this.timeStamp = LocalDateTime.now().withNano(0);
    }
}