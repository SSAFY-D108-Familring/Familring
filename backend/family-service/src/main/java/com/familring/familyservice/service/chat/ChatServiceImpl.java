package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dao.ChatRepository;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private static final int DEFAULT_PAGE_SIZE = 50; // 기본 페이지 사이즈 설정

    @Override
    public Page<Chat> getMessagesByFamilyId(String familyId, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return chatRepository.findByFamilyId(familyId, pageable);
    }

    @Override
    @Transactional
    public Mono<Chat> sendMessage(ChatSendRequest chatSendRequest) {
        // ChatSendRequest를 Chat 객체로 변환
        Chat chat = Chat.builder()
                .familyId(chatSendRequest.getFamilyId())
                .userId(chatSendRequest.getUserId())
                .comment(chatSendRequest.getComment())
                .createdAt(LocalDateTime.now())
                .isVote(chatSendRequest.isVote())
                .build();

        // 투표 메시지일 경우에만 voteTitle 설정
        if (chatSendRequest.isVote()) {
            chat.setVoteTitle(chatSendRequest.getVoteTitle());
        }

        // MongoDB에 저장하고 저장된 Chat 객체 반환
        return chatRepository.save(chat);
    }
}
