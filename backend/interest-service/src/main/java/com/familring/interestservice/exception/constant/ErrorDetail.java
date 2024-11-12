package com.familring.interestservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    NOT_FOUND_INTEREST("I0001", HttpStatus.NOT_FOUND, "관심사를 찾지 못했습니다."),
    NOT_FOUND_INTEREST_ANSWER("I0002", HttpStatus.NOT_FOUND, "관심사 답변을 찾지 못했습니다."),
    EXIST_SELECT_QUESTION_ANSWER("I0003", HttpStatus.CONFLICT, "이미 관심사가 선정되었습니다."),
    INVALID_INTEREST_MISSION_END_DATE("I0004", HttpStatus.BAD_REQUEST, "인증 기간은 오늘 이후로 설정해야 합니다."),
    EXIST_INTEREST_MISSION_END_DATE("I0005", HttpStatus.CONFLICT, "이미 인증 기간을 설정했습니다."),
    NOT_FOUND_INTEREST_MISSION_END_DATE("I0006", HttpStatus.NOT_FOUND, "인증 기간을 설정하지 않았습니다."),
    EXIST_INTEREST_MISSION("I0007", HttpStatus.CONFLICT, "이미 관심사 인증을 했습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}