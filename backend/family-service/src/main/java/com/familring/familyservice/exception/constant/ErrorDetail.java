package com.familring.familyservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {
    // Family
    NOT_FOUND_FAMILY("F0001", HttpStatus.NOT_FOUND, "가족을 찾을 수 없습니다."),
    ALREADY_IN_FAMILY("F0002", HttpStatus.BAD_REQUEST, "이미 해당 가족의 구성원입니다."),
    ALREADY_ROLE_IN_FAMILY("F0003", HttpStatus.CONFLICT, "엄마 또는 아빠가 가족 구성원에 이미 존재합니다."),

    // ChatResponse
    NOT_FOUND_CHAT_ROOM("C0001", HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),
    NOT_FOUND_VOTE("V0001", HttpStatus.NOT_FOUND, "투표를 찾을 수 없습니다."),
    ALREADY_PARTICIPATED("V0002", HttpStatus.CONFLICT, "사용자가 이미 투표에 참여했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}