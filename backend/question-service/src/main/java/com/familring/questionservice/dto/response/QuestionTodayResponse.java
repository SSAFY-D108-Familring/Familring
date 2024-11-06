package com.familring.questionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInfoResponse {

    private Long questionId;
    private String questionContent; // 질문 내용
    private List<QuestionAnswerItem> items; // 작성자 목록 조회

}
