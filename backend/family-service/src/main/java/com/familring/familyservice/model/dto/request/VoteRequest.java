package com.familring.familyservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {
    private Long senderId; // 발신자 id
    private String content; // 투표 응답
}
