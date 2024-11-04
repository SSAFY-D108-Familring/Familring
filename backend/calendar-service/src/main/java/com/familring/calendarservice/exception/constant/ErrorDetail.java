package com.familring.calendarservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    // Schedule
    NOT_FOUND_SCHEDULE("S0001", HttpStatus.NOT_FOUND, "존재하지 않는 스케줄입니다."),
    INVALID_SCHEDULE_REQUEST("S0002", HttpStatus.BAD_REQUEST, "유효하지 않은 스케줄 요청입니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
