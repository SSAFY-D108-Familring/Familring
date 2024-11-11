package com.familring.interestservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InterestAnswerStatusResponse {
    private Boolean answerStatusMine; // 답변 작성 유무
    private Boolean answerStatusFamily; // 가족 답변 작성 유무
    private String content;
}
