package com.familring.interestservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    NOT_FOUND_INTEREST("I0001", HttpStatus.NOT_FOUND, "관심사를 찾지 못했습니다."),
    NOT_FOUND_INTEREST_ANSWER("I0002", HttpStatus.NOT_FOUND, "작성한 관심사 답변을 찾지 못했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}