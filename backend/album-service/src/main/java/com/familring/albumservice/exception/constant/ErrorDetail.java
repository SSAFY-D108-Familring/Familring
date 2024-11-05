package com.familring.albumservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    INVALID_ALBUM_PARAMETER("A0001", HttpStatus.BAD_REQUEST, "요청한 타입의 앨범을 생성하기 위한 파라미터가 올바르지 않습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
