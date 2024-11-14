package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.response.ChatResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatRoomService {

    Page<ChatResponse> findPagedChatByRoomId(Long roomId, Long userId, int page, int size);

    void notifyReadStatusUpdate(Long roomId);

    void notifyRoomExit(Long roomId, Long userId);
}
