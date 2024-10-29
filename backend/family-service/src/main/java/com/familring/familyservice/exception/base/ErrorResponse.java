package com.familring.familyservice.exception.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private String message;

    public ErrorResponse(String message) {
        this.timeStamp = LocalDateTime.now().withNano(0);
        this.message = message;
    }
}