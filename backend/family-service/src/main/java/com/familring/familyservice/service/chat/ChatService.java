package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.ChatDTO;
import com.familring.familyservice.model.dto.ChatEntity;

import java.util.List;

public interface ChatService {

    ChatEntity createChat(Long roomId, Long senderId, String senderNickname, String senderProfileImage, String content, boolean messageChecked, String sendTime);

    List<ChatDTO> findAllChatByRoomId(Long roomId, Long userId);

    void markChatAsRead(String chatId);
}
