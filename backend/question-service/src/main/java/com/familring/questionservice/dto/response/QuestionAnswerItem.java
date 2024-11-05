package com.familring.questionservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerItem {

    private Long answerId;
    private String userNickname;
    private String userZodiacSign;
    private String userColor;
    private String answerContent;
    private Boolean answerStatus; // 답변 여부

}
