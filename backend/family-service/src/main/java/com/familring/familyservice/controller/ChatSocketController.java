package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import com.familring.familyservice.model.dto.request.VoteParticipationRequest;
import com.familring.familyservice.service.chat.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@Log4j2
@Tag(name = "채팅 소켓 컨트롤러", description = "채팅관련 기능 수행")
public class ChatSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    @MessageMapping("/chat")
    public void setMsg(ChatSendRequest chatSendRequest) {
        Mono<Chat> response = chatService.sendMessage(chatSendRequest);
        log.info(response);
        messagingTemplate.convertAndSend("/topic/chat/" + chatSendRequest.getFamilyId(), response);
    }

    @MessageMapping("/chat/vote")
    public void participateInVote(VoteParticipationRequest voteRequest) {
        // 사용자 투표 내용 먼저 전송
        messagingTemplate.convertAndSend("/topic/chat/" + voteRequest.getFamilyId(), voteRequest.getVoteComment());
        log.info("User: {}", voteRequest.getUserId());
        log.info("Comment: {}", voteRequest.getVoteComment());

        // 비동기로 투표 참여를 처리
        chatService.participateInVote(voteRequest);
        log.info("{}에 해당하는 투표가 아직 종료되지 않았습니다.", voteRequest.getVoteId());
    }
}
