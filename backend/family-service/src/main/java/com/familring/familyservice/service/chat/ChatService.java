package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;

public interface ChatService {

    // 채팅 생성
    Chat createChatOrVoiceOrPhotoOrVote(Long roomId, ChatRequest chatRequest);
    Chat createChatVoteResponse(Long roomId, String voteId, ChatRequest chatRequest);
    Chat createChatVoteResult(Long roomId, String voteId, ChatRequest chatRequest);

    // 채팅 응답 찾기
    ChatResponse findChat(Chat chat, Long userId);
}
