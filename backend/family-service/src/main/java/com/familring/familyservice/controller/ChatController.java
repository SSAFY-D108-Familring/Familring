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
    public ChatDTO chatting(@DestinationVariable Long roomId, ChatDTO chatDto) {
        log.info("[ChatController - chatting] 채팅 메시지 정보: roomId={}, chatDto={}", roomId, chatDto );
        ChatEntity chatEntity = chatService.createChat(chatDto);

        log.info("[ChatController - chatting] 채팅 메시지 저장 완료: chatId={}, roomId={}", chatEntity.getId(), roomId);

        return ChatDTO.toChatDTO(chatEntity);
    }
}
