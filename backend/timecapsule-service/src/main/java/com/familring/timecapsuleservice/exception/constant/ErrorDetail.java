package com.familring.familyservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    // Family
    NOT_FOUND_FAMILY("F0001", HttpStatus.NOT_FOUND, "가족을 찾지 못했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}