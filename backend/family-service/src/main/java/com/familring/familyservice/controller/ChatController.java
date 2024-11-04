package com.familring.familyservice.controller;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
@Tag(name = "채팅 컨트롤러", description = "채팅관련 기능 수행")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat")
    @Operation(summary = "채팅 내용 조회", description = "가족 내 채팅 내용 조회")
    public ResponseEntity<BaseResponse<Page<Chat>>> getChatRoomMessages(
            @RequestParam String familyId,
            @RequestParam int page) {
        Page<Chat> chatPage = chatService.getMessagesByFamilyId(familyId, page);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "채팅 메시지 조회 성공", chatPage));
    }

    @GetMapping("/chat/{voteId}")
    @Operation(summary = "투표 참여자 조회", description = "voteId에 해당하는 투표의 참여자 조회")
    public ResponseEntity<BaseResponse<List<UserInfoResponse>>> getVoteParticipants(@PathVariable String voteId) {
        // 서비스에서 Mono를 동기적으로 처리하여 List<UserInfoResponse>로 반환
        List<UserInfoResponse> response = chatService.getVoteParticipants(voteId).block();

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "채팅 내 투표 참가자 조회 성공", response));
    }

    @MessageMapping("/chat")
    public void setMsg(ChatSendRequest chatSendRequest) {
        // MongoDB에 메시지 저장 후, 해당 메시지를 구독 중인 클라이언트에게 전송
        Mono<Chat> response = chatService.sendMessage(chatSendRequest);
        log.info(response);
        messagingTemplate.convertAndSend("/topic/chat" + chatSendRequest.getFamilyId(), response);
    }
}
