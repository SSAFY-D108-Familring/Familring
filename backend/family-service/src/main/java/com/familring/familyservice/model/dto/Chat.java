package com.familring.familyservice.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "family_chat")
public class Chat {

    @Id
    private String id; // MongoDB의 Document ID

    private String familyId; // 가족 ID (가족 단위로 채팅이 구성됨)
    private String userId;   // 메시지를 보낸 사용자 ID
    private String comment;  // 채팅 내용
    private LocalDateTime createdAt; // 채팅 전송 시간

    private boolean isVote; // 투표 여부 (true면 투표 메시지, false면 일반 메시지)
    private String voteTitle; // 투표 제목 (isVote가 true일 때만 사용)

    public Chat(String familyId, String userId, String comment, boolean isVote, String voteTitle) {
        this.familyId = familyId;
        this.userId = userId;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.isVote = isVote;
        this.voteTitle = isVote ? voteTitle : null; // 투표가 아닌 경우 제목은 null
    }
}
