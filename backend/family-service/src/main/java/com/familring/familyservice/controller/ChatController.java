package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;
import com.familring.familyservice.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
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
    public ChatResponse chatting(@DestinationVariable Long roomId, ChatRequest chatRequest) {
        log.info("[chatting] 채팅 메시지 정보: roomId={}, senderId={}, comment={}", roomId, chatRequest.getSenderId(), chatRequest.getContent());
        Chat chat = chatService.createChatAndVote(roomId, chatRequest);

        log.info("[chatting] 채팅 메시지 저장 완료: chatId={}, roomId={}", chat.getChatId(), roomId);
        return chatService.findChat(chat, chatRequest.getSenderId());
    }

    @MessageMapping("/{roomId}/vote")
    @SendTo("/room/{roomId}")
    public ChatResponse participateInVote
            (@DestinationVariable Long roomId, ChatRequest chatRequest) {
        log.info("[participateInVote] 채팅(투표 응답) 메시지 정보: roomId={}, senderId={}, voteId={}", roomId, chatRequest.getSenderId(), chatRequest.getVoteId());
        Chat chat = chatService.createChatVoteResponse(roomId, chatRequest.getVoteId(), chatRequest);

        log.info("[participateInVote] 채팅(투표 응답) 메시지 저장 완료: chatId={}, roomId={}", chat.getChatId(), roomId);
        return chatService.findChat(chat, chatRequest.getSenderId());
    }
}
