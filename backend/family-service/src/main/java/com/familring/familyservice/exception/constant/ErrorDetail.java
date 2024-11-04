package com.familring.familyservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    // Family
    NOT_FOUND_FAMILY("F0001", HttpStatus.NOT_FOUND, "가족을 찾지 못했습니다."),
    ALREADY_IN_FAMILY("F0002", HttpStatus.BAD_REQUEST, "이미 해당 가족의 구성원입니다."),

    // Chat
    ALREADY_PARTICIPATED("V0001", HttpStatus.CONFLICT, "해당 사용자가 이미 투표에 참여했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}