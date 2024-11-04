package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

public interface ChatService {
    // 채팅 조회
    Page<Chat> getMessagesByFamilyId(String familyId, int page);

    // 채팅 전송
    Mono<Chat> sendMessage(ChatSendRequest chatSendRequest);
}
