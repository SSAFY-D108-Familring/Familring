package com.familring.familyservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "채팅 컨트롤러", description = "채팅관련 기능 수행")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    @Operation(summary = "채팅 내용 조회", description = "가족 내 채팅 내용 조회")
    public ResponseEntity<BaseResponse<Page<Chat>>> getChatRoomMessages(
            @Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
            @RequestParam Long familyId, @RequestParam int page) {
        Page<Chat> chatPage = chatService.getMessagesByFamilyId(userId.toString(), familyId.toString(), page);
        log.info("ChatPage: {}", chatPage.getTotalPages());

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "채팅 메시지 조회 성공", chatPage));
    }

    @GetMapping("/{voteId}")
    @Operation(summary = "투표 참여자 조회", description = "voteId에 해당하는 투표의 참여자 조회")
    public ResponseEntity<BaseResponse<List<UserInfoResponse>>> getVoteParticipants(@PathVariable String voteId) {
        List<UserInfoResponse> response = chatService.getVoteParticipants(voteId).block();
        log.info("userList: {}", response);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "채팅 내 투표 참가자 조회 성공", response));
    }
}
