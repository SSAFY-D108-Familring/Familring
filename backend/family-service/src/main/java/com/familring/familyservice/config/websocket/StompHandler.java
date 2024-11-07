package com.familring.familyservice.config.websocket;

import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.chat.ChatService;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class StompHandler implements ChannelInterceptor {

    private final ChatService chatService;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        handleMessage(accessor.getCommand(), accessor, message.getHeaders());

        return message;
    }

    private void handleMessage(StompCommand command, StompHeaderAccessor accessor, MessageHeaders headers) {
        switch (command) {
            case CONNECT:
                log.debug("[Handler - handleMessage] CONNECT");
                break;
            case SUBSCRIBE:
                log.debug("[Handler - handleMessage] SUBSCRIBE");
                Long roomId = connectToChatRoom(accessor, headers);
                log.debug("[Handler - handleMessage] 채팅방 구독한 채팅 방 = {}", roomId);
                break;
            case SEND:
                log.debug("[Handler - handleMessage] SEND");
                log.debug("[Handler - handleMessage] 메세지 맵핑 주소 = {}", accessor.getDestination());
                break;
            case DISCONNECT:
                log.debug("[Handler - handleMessage] DISCONNECT");
                break;
        }
    }

    private Long connectToChatRoom(StompHeaderAccessor accessor, MessageHeaders headers) {
        log.debug("[Handler - connectToChatRoom] 시작");
        Long roomId = getChatRoomNo(headers);
        log.debug("[Handler - connectToChatRoom] 채팅방 번호 = {}", roomId);
        Long userId = Long.valueOf(accessor.getFirstNativeHeader("X-User-ID"));
        log.debug("[Handler - connectToChatRoom] userId = {}", userId);
        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();
        log.debug("[Handler - connectToChatRoom] 회원 이름 = {}", user.getUserNickname());
        chatService.connectChatRoom(roomId, userId);
        log.debug("[Handler - connectToChatRoom] connectChatRoom() 연결 완료");

        return roomId;
    }

    private String getRoomId(String destination) {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return null;
        }
    }

    private Long getChatRoomNo(MessageHeaders headers) {
        String roomId = getRoomId(Optional.ofNullable((String)
                headers.get("simpDestination")).orElse("InvalidRoomId"));
        return Long.valueOf(roomId);
    }
}
