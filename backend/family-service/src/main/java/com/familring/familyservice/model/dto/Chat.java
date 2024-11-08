package com.familring.familyservice.model.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chats")
@ToString
public class Chat {

    @Id
    private String chatId;
    private Long roomId; // 채팅 방의 id == familyId

    // 발신자 정보
    private Long senderId; // 발신자 id

    // 채팅 정보
    private String content; // 채팅 내용
    private LocalDateTime createdAt; // 채팅 발신 시간

    // 투표 정보
    private boolean isVote; // 투표 여부
    private String voteId; // 투표 id
    private boolean isVoteResponse; // 투표 응답 여부
    private String responseOfVote; // 투표 응답

    public boolean getIsVote() {
        return isVote;
    }

    public void setIsVote(boolean isVote) {
        this.isVote = isVote;
    }

    public boolean getIsVoteResponse() {
        return isVoteResponse;
    }

    public void setIsVoteResponse(boolean isVoteResponse) {
        this.isVoteResponse = isVoteResponse;
    }
}