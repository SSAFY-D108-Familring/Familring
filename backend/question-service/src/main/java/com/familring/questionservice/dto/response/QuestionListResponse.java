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
public class QuestionListResponse {
    int pageNo;
    boolean isLast; // 현재 슬라이스가 마지막인지 여부
    boolean hasNext; // 다음 슬라이스가 있는지 여부
    List<QuestionItem> items;
}
