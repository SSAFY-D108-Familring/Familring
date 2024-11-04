package com.familring.timecapsuleservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    NOT_FOUND_FAMILY("F0001", HttpStatus.NOT_FOUND, "가족을 찾지 못했습니다."),
    NOT_FOUND_TIME_CAPSULE("T0001", HttpStatus.NOT_FOUND, "타입캡슐을 찾지 못했습니다."),
    EXIST_TIME_CAPSULE("T0002", HttpStatus.CONFLICT, "작성 중인 타임캡슐이 있습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}