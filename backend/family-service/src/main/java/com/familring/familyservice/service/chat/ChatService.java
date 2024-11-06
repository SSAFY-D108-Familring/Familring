package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.ChatDTO;
import com.familring.familyservice.model.dto.ChatEntity;
import com.familring.familyservice.model.dto.request.ChatRequest;

import java.util.List;

public interface ChatService {

    ChatEntity createChat(Long roomId, ChatRequest chatRequest);

    List<ChatDTO> findAllChatByRoomId(Long roomId, Long userId);
}
