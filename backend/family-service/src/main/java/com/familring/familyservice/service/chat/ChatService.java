package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;

import java.util.List;

public interface ChatService {

    // 채팅 생성
    Chat createChatAndVote(Long roomId, ChatRequest chatRequest);
    Chat createChatVoteResponse(Long roomId, String voteId, ChatRequest chatRequest);
    Chat createChatVoteResult(Long roomId, String voteId, ChatRequest chatRequest);

    // 채팅
    List<ChatResponse> findAllChatByRoomId(Long roomId, Long userId);
    ChatResponse findChat(Chat chat, Long userId);

    // 채팅방 연결
    void connectChatRoom(Long roomId, Long userId);

}
