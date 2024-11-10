package com.familring.interestservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestAnswerSelectedResponse {

    private String userNickname; // 누구의 관심사가 선정되었는지
    private String content; // 선정된 관심사 내용

}
