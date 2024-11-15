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
    EXIST_TIME_CAPSULE("T0002", HttpStatus.CONFLICT, "작성 중인 타임캡슐이 있습니다."),
    EXIST_TIME_CAPSULE_ANSWER("T0003", HttpStatus.CONFLICT, "이미 답변을 작성했습니다."),
    EXPIRED_TIME_CAPSULE_ANSWER("T0004", HttpStatus.FORBIDDEN, "타임캡슐 답변 작성 기한이 지났습니다."),
    FAILED_CREATE_TIME_CAPSULE("T0005", HttpStatus.BAD_REQUEST, "타임캡슐 생성에 실패했습니다."),
    TIME_CAPSULE_MINIMUM_DURATION("T0006", HttpStatus.BAD_REQUEST, "타임캡슐 생성은 최소 하루 이상이어야 합니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}