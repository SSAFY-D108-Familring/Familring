package com.familring.familyservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.familyservice.model.dto.response.ChatPageResponse;
import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.service.chat.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "채팅방 컨트롤러", description = "채팅방 관련 기능 수행")
@Log4j2
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("enter/{roomId}")
    @Operation(summary = "채팅방 입장", description = "roomId에 해당하는 채팅방의 채팅 List를 페이징 처리하여 조회")
    public ResponseEntity<?> joinRoom(
            @PathVariable Long roomId, @RequestHeader("X-User-ID") Long userId,
            @RequestParam(value = "page") int page, @RequestParam(value = "size") int size) {
        try {
            log.info("[joinRoom] 채팅방 입장 요청 roomId={}, userId={}, page={}, size={}", roomId, userId, page, size);
            Page<ChatResponse> chatPage = chatRoomService.findPagedChatByRoomId(roomId, userId, page, size);

            ChatPageResponse chatPageResponse = ChatPageResponse.builder()
                    .hasNext(chatPage.hasNext())
                    .chatList(chatPage.getContent())
                    .build();

            log.info("[joinRoom] 채팅방 입장 성공 roomId={}, {}개의 메시지가 조회됨", roomId, chatPageResponse.getChatList().size());
            return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "채팅방을 성공적으로 가져와 조회했습니다.", chatPageResponse));

        } catch (Exception e) {
            log.error("[joinRoom] 채팅방 입장 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }

}
