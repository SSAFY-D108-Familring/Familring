package com.familring.familyservice.model.dto.response;

import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    
    private ObjectId chatId; // 채팅의 id
    private Long roomId; // 채팅 방의 id == familyId
    
    private Long senderId; // 발신자 id
    private UserInfoResponse sender; // 발신자 정보
    
    private String content; // 채팅 내용
    private LocalDateTime createdAt; // 채팅 발신 시간

    private boolean isVote; // 투표 여부
    private Vote vote; // 투표

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
}
