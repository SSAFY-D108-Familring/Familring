package com.familring.interestservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestDetailItem {

    private String userNickname;
    private String userZodiacSign;
    private String photoUrl; // 인증 사진 url

}
