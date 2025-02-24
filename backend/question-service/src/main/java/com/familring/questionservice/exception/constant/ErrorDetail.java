package com.familring.questionservice.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorDetail {

    NOT_FOUND_QUESTION("Q0001", HttpStatus.NOT_FOUND, "질문을 찾지 못했습니다."),
    NOT_FOUND_QUESTION_FAMILY("Q0002", HttpStatus.NOT_FOUND, "가족의 질문을 찾지 못했습니다."),
    EXIST_QUESTION_ANSWER("Q0003", HttpStatus.CONFLICT, "이미 답변을 생성했습니다."),
    NOT_FOUND_QUESTION_ANSWER("Q0004", HttpStatus.NOT_FOUND, "사용자가 작성한 답변을 찾지 못했습니다."),
    INVALID_QUERY_PARAM("Q0005", HttpStatus.BAD_REQUEST, "요청 형식이 잘못되었습니다.");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;
}