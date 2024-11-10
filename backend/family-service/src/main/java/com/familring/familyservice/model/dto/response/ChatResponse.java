package com.familring.familyservice.model.dto.response;

import com.familring.familyservice.model.dto.chat.MessageType;
import com.familring.familyservice.model.dto.chat.Vote;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String chatId; // 채팅의 id
    private Long roomId; // 채팅 방의 id == familyId
    private MessageType messageType; // 메세지의 타입
    
    private Long senderId; // 발신자 id
    private UserInfoResponse sender; // 발신자 정보
    
    private String content; // 채팅 내용
    private LocalDateTime createdAt; // 채팅 발신 시간

    // 투표
    private Vote vote; // 투표

    // 투표 응답
    private String responseOfVote; // 투표 응답
    private boolean isVoteEnd; // 투표 끝남 유무

    // 투표 결과
    private Map<String, Integer> resultOfVote; // 투표 결과

    // 읽음 처리
    private int unReadMembers; // 안읽은 사람 수

    // getter, setter
    public void setVote(Vote vote) {
        this.vote = vote;
    }
}
