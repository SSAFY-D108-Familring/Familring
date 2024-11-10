package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;
import com.familring.familyservice.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
@CrossOrigin("*")
@Log4j2
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatRequest chatRequest) {
        Long roomId = chatRequest.getRoomId();
        log.info("[sendMessage] 채팅방 Id = {}", roomId);

        // 메시지 생성 및 처리
        Chat chat = chatService.createChatAndVote(roomId, chatRequest);
        ChatResponse chatResponse = chatService.findChat(chat, chatRequest.getSenderId());

        template.convertAndSend("/room/" + roomId, chatResponse);
        log.debug("[sendMessage] 일반 메시지 소켓 전송 완료.");
    }

    @MessageMapping("/chat.vote")
    public void participateInVote(ChatRequest chatRequest) {
        Long roomId = chatRequest.getRoomId();
        String voteId = chatRequest.getVoteId();
        log.info("[participateInVote] 채팅방 Id = {}, voteId={}", roomId, voteId);

        // 투표 응답 처리
        Chat chatVoteResponse = chatService.createChatVoteResponse(roomId, voteId, chatRequest);
        ChatResponse chatResponse = chatService.findChat(chatVoteResponse, chatRequest.getSenderId());

        template.convertAndSend("/room/" + roomId, chatResponse);
        log.info("[participateInVote] 투표 응답 소켓 전송 완료.");

        // 모든 투표가 완료된 경우 투표 결과 전송
        if (chatVoteResponse.getIsVoteEnd()) {
            log.info("[participateInVote] 채팅 결과 소켓 전송");

            Chat chatVoteResult = chatService.createChatVoteResult(roomId, voteId, chatRequest);
            chatResponse = chatService.findChat(chatVoteResult, chatRequest.getSenderId());

            template.convertAndSend("/room/" + roomId, chatResponse);
            log.info("[participateInVote] 투표 결과 소켓 전송 완료.");
        }
    }
}
