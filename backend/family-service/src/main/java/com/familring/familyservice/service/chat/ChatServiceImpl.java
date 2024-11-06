package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.ChatDTO;
import com.familring.familyservice.model.dto.ChatEntity;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRepository;
import com.familring.familyservice.service.chat.event.ChatCreatedEvent;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
    public ChatEntity createChat(Long roomId, Long senderId, String senderNickname, String senderProfileImage, String content, boolean messageChecked, String sendTime) {
        ChatEntity chatEntity = chatRepository.save(ChatEntity.createChat(
                roomId,
                senderId,
                senderNickname,
                senderProfileImage,
                content,
                true,
                sendTime
        ));

        // ChatCreatedEvent 이벤트를 발행하여 다른 서비스나 컴포넌트에서 이 이벤트를 처리할 수 있게 함
        eventPublisher.publishEvent(new ChatCreatedEvent(roomId, content, sendTime));

        return chatEntity;
    }

    @Override
    public List<ChatDTO> findAllChatByRoomId(Long roomId, Long userId) {
        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();

        return chatRepository.findAllByRoomId(roomId).stream()
                .map(chatEntity -> {


                    if(user.getUserId() != chatEntity.getSenderId()){
                        chatEntity.markAsRead();
                    }

                    chatRepository.save(chatEntity);

                    return ChatDTO.builder()
                            .id(chatEntity.getId().toHexString())
                            .roomId(chatEntity.getRoomId())
                            .senderId(chatEntity.getSenderId())
                            .senderNickname(chatEntity.getSenderNickname())
                            .senderProfileImage(chatEntity.getSenderProfileImage())
                            .content(chatEntity.getContent())
                            .messageChecked(chatEntity.getMessageChecked())
                            .sendTime(chatEntity.getSendTime())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markChatAsRead(String chatId) {
        ChatEntity chatEntity = chatRepository.findById(Long.valueOf(new ObjectId(chatId).getTimestamp()))
                .orElseThrow(() -> new NoSuchElementException("Chat not found with id: " + chatId));
        chatEntity.markAsRead();

        chatRepository.save(chatEntity);
    }
}
