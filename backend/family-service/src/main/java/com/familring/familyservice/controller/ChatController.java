package com.familring.familyservice.controller;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import com.familring.familyservice.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat")
    public ResponseEntity<BaseResponse<Page<Chat>>> getChatRoomMessages(
            @RequestParam String familyId,
            @RequestParam int page) {
        Page<Chat> chatPage = chatService.getMessagesByFamilyId(familyId, page);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "채팅 메시지 조회 성공", chatPage));
    }

    @MessageMapping("/chat")
    public void setMsg(ChatSendRequest chatSendRequest) {
        // MongoDB에 메시지 저장 후, 해당 메시지를 구독 중인 클라이언트에게 전송
        Mono<Chat> response = chatService.sendMessage(chatSendRequest);
        log.info(response);
        messagingTemplate.convertAndSend("/topic/chat" + chatSendRequest.getFamilyId(), response);
    }
}
