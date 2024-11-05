package com.familring.questionservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswerUpdateRequest {

    private String content;
    private LocalDate modifiedAt;

}
