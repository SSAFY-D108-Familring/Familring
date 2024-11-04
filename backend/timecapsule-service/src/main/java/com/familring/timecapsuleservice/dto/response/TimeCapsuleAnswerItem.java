package com.familring.timecapsuleservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeCapsuleAnswerItem {
    private String userNickname;
    private String userZodiacSign;
    private String userColor;
    private String content;
    private LocalDate date; // 답변 작성 일자
}
