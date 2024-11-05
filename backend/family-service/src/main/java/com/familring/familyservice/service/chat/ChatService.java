package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import com.familring.familyservice.model.dto.request.VoteParticipationRequest;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ChatService {
    // 채팅 조회
    Page<Chat> getMessagesByFamilyId(String familyId, int page);

    // 투표 참가자 조회
    Mono<List<UserInfoResponse>> getVoteParticipants(String voteId);

    // 채팅 전송
    Mono<Chat> sendMessage(ChatSendRequest chatSendRequest);

    // 투표 참여
    void participateInVote(VoteParticipationRequest voteRequest);
}
