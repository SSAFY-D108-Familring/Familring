package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.ChatDTO;
import com.familring.familyservice.model.dto.ChatEntity;

import java.util.List;

public interface ChatService {

    ChatEntity createChat(ChatDTO chatDto);

    List<ChatDTO> findAllChatByRoomId(Long roomId, Long userId);
}
