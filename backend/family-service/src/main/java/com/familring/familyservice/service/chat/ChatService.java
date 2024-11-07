package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;

import java.util.List;

public interface ChatService {

    Chat createChat(Long roomId, ChatRequest chatRequest);

    List<ChatResponse> findAllChatByRoomId(Long roomId, Long userId);

    ChatResponse findChat(Chat chat, Long userId);

    void connectChatRoom(Long roomId, Long userId);
}
