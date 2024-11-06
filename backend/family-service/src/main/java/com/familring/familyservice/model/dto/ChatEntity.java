package com.familring.familyservice.model.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chats")
@ToString
public class ChatEntity {

    @Id
    private ObjectId id;
    private Long roomId; // 채팅 방의 id == familyId

    private Long senderId; // 발신자 id
    private String senderNickname; // 발신자 nickname
    private String senderZodiacSignImageUrl; // 발신자 프로필 배경색
    private String senderColor; // 발신자 프로필 배경색

    private String content; // 채팅 내용
    private String createdAt; // 채팅 발신 시간


    public static ChatEntity createChat(Long roomId, Long senderId, String senderNickname, String senderZodiacSignImageUrl, String senderColor, String content, String createdAt) {
        return ChatEntity.builder()
                .roomId(roomId)
                .senderId(senderId)
                .senderNickname(senderNickname)
                .senderZodiacSignImageUrl(senderZodiacSignImageUrl)
                .senderColor(senderColor)
                .content(content)
                .createdAt(createdAt)
                .build();
    }
}