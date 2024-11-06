package com.familring.familyservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    
    private String id; // 채팅의 id
    private Long roomId; // 채팅 방의 id == familyId
    
    private Long senderId; // 발신자 id
    private String senderNickname; // 발신자 nickname
    private String senderZodiacSignImageUrl; // 발신자 띠 사진
    private String senderColor; // 발신자 프로필 배경색
    
    private String content; // 채팅 내용
    private String createdAt; // 채팅 발신 시간

    public static ChatDTO toChatDTO(ChatEntity chatEntity) {
        return ChatDTO.builder()
                .id(chatEntity.getId().toHexString())
                .roomId(chatEntity.getRoomId())
                .senderId(chatEntity.getSenderId())
                .senderNickname(chatEntity.getSenderNickname())
                .senderZodiacSignImageUrl(chatEntity.getSenderZodiacSignImageUrl())
                .senderColor(chatEntity.getSenderColor())
                .content(chatEntity.getContent())
                .createdAt(chatEntity.getCreatedAt())
                .build();
    }
}
