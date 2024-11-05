package com.familring.questionservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    NOT_FOUND_QUESTION("Q0001", HttpStatus.NOT_FOUND, "질문을 찾지 못했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}