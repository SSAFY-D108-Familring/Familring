package com.familring.familyservice.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    // 발신자 정보
    private Long senderId; // 발신자 id
    private String content; // 채팅 내용

    // 투표 정보
    // 투표 생성
    @JsonProperty("isVote")
    private boolean isVote; // 투표 여부
    private String voteTitle; // 투표 제목

    // 투표 응답
    private String voteId; // 투표 Id
    private boolean isVoteResponse; // 투표 응답 여부
    private String responseOfVote; // 투표 응답

    public boolean isVote() { // 정확히 `isVote()`으로 설정
        return isVote;
    }

    public boolean getIsVoteResponse() {
        return isVoteResponse;
    }
}
