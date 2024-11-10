package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.response.ChatResponse;

import java.util.List;

public interface ChatRoomService {

    List<ChatResponse> findPagedChatByRoomId(Long roomId, Long userId, int page, int size);

    void connectChatRoom(Long roomId, Long userId);

    void disconnectChatRoom(Long roomId, Long userId);

    void notifyReadStatusUpdate(Long roomId);

    void notifyRoomExit(Long roomId, Long userId);
}
