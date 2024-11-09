package com.familring.familyservice.model.dto.response;

import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String chatId; // 채팅의 id
    private Long roomId; // 채팅 방의 id == familyId
    
    private Long senderId; // 발신자 id
    private UserInfoResponse sender; // 발신자 정보
    
    private String content; // 채팅 내용
    private LocalDateTime createdAt; // 채팅 발신 시간

    // 투표
    private boolean isVote; // 투표 여부
    private Vote vote; // 투표

    // 투표 응답
    private boolean isVoteResponse; // 투표 응답 여부
    private String responseOfVote; // 투표 응답
    private boolean voteEnd; // 투표 끝남 유무

    // 투표 결과
    private boolean isVoteResult; // 투표 결과 여부
    private Map<String, Integer> resultOfVote; // 투표 결과

    public boolean getIsVote() {
        return isVote;
    }

    public void setIsVote(boolean isVote) {
        this.isVote = isVote;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public boolean getIsVoteResponse() {
        return isVoteResponse;
    }

    public void setIsVoteResponse(boolean isVoteResponse) {}

    public boolean getIsVoteResult() {
        return isVoteResult;
    }

    public void setIsVoteResult(boolean isVoteResult) {
        this.isVoteResult = isVoteResult;
    }
}
