package com.familring.calendarservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    // Schedule
    SCHEDULE_NOT_FOUND("S0001", HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),
    INVALID_SCHEDULE_REQUEST("S0002", HttpStatus.BAD_REQUEST, "유효하지 않은 일정 요청입니다."),

    // Daily
    DAILY_NOT_FOUND("D0001", HttpStatus.NOT_FOUND, "존재하지 않는 일상입니다."),
    INVALID_DAILY_REQUEST("D0002", HttpStatus.BAD_REQUEST, "유효하지 않은 일상 요청입니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
