package com.familring.familyservice.service.chat.event;

import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Log4j2
public class VoteResultHandler {

    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate template;

    @Async
    @EventListener
    @Transactional
    public void handleVoteCompleted(VoteCompletedEvent event) {
        log.info("[handleVoteCompleted] 투표 완료 이벤트 수신: roomId={}, voteId={}",
                event.getRoomId(), event.getVoteId());

        // 투표 결과 채팅 객체 생성
        Chat voteResultChat = Chat.builder()
                .roomId(event.getRoomId())
                .senderId(event.getSenderId())
                .content(event.getVoteTitle())
                .createdAt(event.getCreatedAt())
                .isVote(false)
                .voteId(event.getVoteId())
                .isVoteResponse(false)
                .responseOfVote("")
                .isVoteResult(true)
                .resultOfVote(event.getVoteResult())
                .build();

        // 투표 결과 채팅 저장
        chatRepository.save(voteResultChat);
        log.info("[handleVoteCompleted] 투표 결과 채팅 저장 완료: chatId={}", voteResultChat.getChatId());

        // WebSocket으로 결과 전송
        template.convertAndSend("/room/" + event.getRoomId(), voteResultChat);
        log.info("[handleVoteCompleted] 투표 결과 WebSocket 전송 완료");
    }
}
