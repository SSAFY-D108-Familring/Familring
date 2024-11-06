package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.ChatDTO;
import com.familring.familyservice.model.dto.ChatEntity;
import com.familring.familyservice.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
@CrossOrigin("*")
@Log4j2
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public ChatDTO chatting(@DestinationVariable Long roomId, ChatDTO chatDTO) {
        log.info("채팅 메시지 수신: roomId={}, senderId={}, content={}", roomId, chatDTO.getSenderId(), chatDTO.getContent());

        ChatEntity chatEntity = chatService.createChat(
                roomId, // 채팅방 ID
                chatDTO.getSenderId(), // 발신자 ID
                chatDTO.getSenderNickname(), // 발신자 닉네임
                chatDTO.getSenderProfileImage(), // 발신자 프로필 이미지 URL
                chatDTO.getContent(), // 채팅 메시지 내용
                false, // 읽음 여부
                chatDTO.getSendTime() // 메시지 전송 시간
        );

        log.info("채팅 메시지 저장 완료: chatId={}, roomId={}", chatEntity.getId(), roomId);

        return ChatDTO.toChatDTO(chatEntity);
    }
}
