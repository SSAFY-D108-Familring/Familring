package com.familring.interestservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestAnswerResponse {

    private Long userId;
    private String userNickname;
    private String userZodiacSign;
    private String content; // 어떤 관심사인지

}
