package com.familring.questionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionItem {

    private Long questionId;
    private String questionContent; // 질문 내용

}
