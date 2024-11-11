package com.familring.familyservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.familyservice.model.dto.request.FileUploadRequest;
import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;
import com.familring.familyservice.service.chat.ChatService;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "채팅 컨트롤러", description = "채팅 관련 기능 수행")
@Log4j2
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatRequest chatRequest) {
        Long roomId = chatRequest.getRoomId();
        log.info("[sendMessage] 채팅방 Id = {}", roomId);

        // 일반 메시지 생성 및 처리
        Chat chat = chatService.createChatOrVoiceOrVote(roomId, chatRequest);
        ChatResponse chatResponse = chatService.findChat(chat, chatRequest.getSenderId());

        template.convertAndSend("/room/" + roomId, chatResponse);
        log.debug("[sendMessage] 일반 메시지 소켓 전송 완료.");
    }

    @MessageMapping("/chat.voice")
    public void sendVoiceMessage(ChatRequest chatRequest) {
        Long roomId = chatRequest.getRoomId();
        log.info("[sendVoiceMessage] 채팅방 Id = {}", roomId);

        // 음성 메시지 생성 및 처리
        Chat chat = chatService.createChatOrVoiceOrVote(roomId, chatRequest);
        ChatResponse voiceChatResponse = chatService.findChat(chat, chatRequest.getSenderId());

        template.convertAndSend("/room/" + roomId, voiceChatResponse);
        log.debug("[sendVoiceMessage] 음성 메시지 소켓 전송 완료.");
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

    @PostMapping(value = "/voice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "음성 파일 업로드", description = "음성 파일을 S3에 저장한 후 url 반환")
    public ResponseEntity<BaseResponse<String>> uploadVoiceFile
            (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId,
             @RequestPart("fileUploadRequest") FileUploadRequest fileUploadRequest,
             @RequestPart(value = "voice", required = false) MultipartFile voice) {
        String responseUrl = chatService.uploadVoiceFile(userId, fileUploadRequest, voice);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "음성 파일을 S3에 성공적으로 업로드했습니다.", responseUrl));
    }
}
