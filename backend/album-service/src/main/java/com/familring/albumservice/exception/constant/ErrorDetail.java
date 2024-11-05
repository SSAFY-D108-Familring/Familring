package com.familring.albumservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    INVALID_ALBUM_PARAMETER("A0001", HttpStatus.BAD_REQUEST, "요청한 타입의 앨범을 생성하기 위한 파라미터가 올바르지 않습니다."),
    ALBUM_NOT_FOUND("A0002", HttpStatus.NOT_FOUND, "존재하지 않는 앨범입니다."),
    INVALID_ALBUM_REQUEST("A0003", HttpStatus.BAD_REQUEST, "유효하지 않은 앨범 요청입니다.");


    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}
