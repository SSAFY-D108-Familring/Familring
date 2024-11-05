package com.familring.albumservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {;

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
