package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.ChatDTO;
import com.familring.familyservice.model.dto.ChatEntity;
import com.familring.familyservice.model.dto.request.ChatRequest;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRepository;
import com.familring.familyservice.service.chat.event.ChatCreatedEvent;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final UserServiceFeignClient userServiceFeignClient;
    private final ApplicationEventPublisher eventPublisher;

    private final ChatRepository chatRepository;

    @Override
    @Transactional
    public ChatEntity createChat(Long roomId, ChatRequest chatRequest) {
        log.info("[ChatService - createChat] 채팅 메시지 수신: roomId={}, senderId={}, content={}", roomId, chatRequest.getSenderId(), chatRequest.getContent());

        UserInfoResponse user = userServiceFeignClient.getUser(chatRequest.getSenderId()).getData();
        log.info("[ChatService - createChat] 회원 찾기: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());

        ChatEntity chatEntity = chatRepository.save(ChatEntity.createChat(
                roomId,
                user.getUserId(),
                user.getUserNickname(),
                user.getUserZodiacSign(),
                user.getUserColor(),
                chatRequest.getContent(),
                LocalDateTime.now().toString()
        ));
        log.info("[ChatService - createChat] chatRepository 저장 완료");

        // ChatCreatedEvent 이벤트를 발행하여 다른 서비스나 컴포넌트에서 이 이벤트를 처리할 수 있게 함
        eventPublisher.publishEvent(new ChatCreatedEvent(roomId, chatRequest.getContent(), LocalDateTime.now().toString()));
        log.info("[ChatService - createChat] 이벤트 발행 완료");

        return chatEntity;
    }

    @Override
    public List<ChatDTO> findAllChatByRoomId(Long roomId, Long userId) {
        return chatRepository.findAllByRoomId(roomId).stream()
                .map(chatEntity -> ChatDTO.builder()
                        .id(chatEntity.getId().toHexString())
                        .roomId(chatEntity.getRoomId())
                        .senderId(chatEntity.getSenderId())
                        .senderNickname(chatEntity.getSenderNickname())
                        .senderZodiacSignImageUrl(chatEntity.getSenderZodiacSignImageUrl())
                        .senderColor(chatEntity.getSenderColor())
                        .content(chatEntity.getContent())
                        .createdAt(chatEntity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
