package com.familring.familyservice.model.dto.chat;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private MessageType messageType; // 메세지의 타입
    private int familyCount; // 채팅 방 사람 총 수

    // 발신자 정보
    private Long senderId; // 발신자 id

    // 채팅 정보
    private String content; // 채팅 내용
    private LocalDateTime createdAt; // 채팅 발신 시간

    // 투표
    private String voteId; // 투표 id
    
    // 투표 응답
    private String responseOfVote; // 투표 응답
    private boolean isVoteEnd; // 투표 종료 여부
    
    // 투표 결과
    private Map<String, Integer> resultOfVote; // 투표 결과
    
    // 읽음 처리
    private Set<Long> readByUserIds; // 읽음 사람 id 저장

    // getter, setter
    public boolean getIsVoteEnd() {
        return isVoteEnd;
    }

    public void setIsVoteEnd(boolean isVoteEnd) {
        this.isVoteEnd = isVoteEnd;
    }

    // 읽음 처리 메소드
    public void markAsRead(Long userId) {
        readByUserIds.add(userId);
    }

    // 몇 명이 읽었는지 계산하는 메소드
    public int getReadCount() {
        return readByUserIds.size();
    }
}