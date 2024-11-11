package com.familring.notificationservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    // Notification
    NOT_FOUND_NOTIFICATION("N0001", HttpStatus.NOT_FOUND, "해당 알림을 찾을 수 없습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
