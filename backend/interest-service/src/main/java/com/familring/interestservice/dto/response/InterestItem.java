package com.familring.interestservice.dto.response;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InterestItem {

    private Long interestId;
    private int index; // 몇번째 관심사인지
    private String userNickname; // 누구의 관심사가 선정되었는지
    private String content; // 선정된 관심사 내용

}
