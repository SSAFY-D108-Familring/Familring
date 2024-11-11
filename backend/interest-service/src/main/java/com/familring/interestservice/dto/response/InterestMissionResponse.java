package com.familring.interestservice.dto.response;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestMissionResponse {

    private String photoUrl;
    private String userZodiacSign;
    private String userNickname;

}
