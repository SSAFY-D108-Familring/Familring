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
        StompCommand command = accessor.getCommand();

        handleMessage(command, accessor, message.getHeaders());

        return message;
    }

    private void handleMessage(StompCommand command, StompHeaderAccessor accessor, MessageHeaders headers) {
        switch (command) {
            case CONNECT:
                log.info("[Handler - handleMessage] CONNECT");
                String userIdHeader = accessor.getFirstNativeHeader("X-User-ID");
                log.info("[Handler - handleMessage] WebSocket 연결 시 X-User-ID 헤더: {}", userIdHeader);

                if (userIdHeader == null) {
                    log.error("X-User-ID 헤더가 누락되었습니다.");
                    throw new IllegalArgumentException("X-User-ID 헤더가 필요합니다.");
                }

                Long userId = Long.valueOf(userIdHeader);
                accessor.getSessionAttributes().put("userId", userId); // 세션에 저장
                break;

            case SUBSCRIBE:
                log.info("[Handler - handleMessage] SUBSCRIBE");
                userId = (Long) accessor.getSessionAttributes().get("userId");
                Long roomId = connectToChatRoom(headers, userId);
                log.info("[Handler - handleMessage] 채팅방 구독한 채팅 방 = {}", roomId);
                break;

            case SEND:
                log.info("[Handler - handleMessage] SEND");
                log.info("[Handler - handleMessage] 메세지 맵핑 주소 = {}", accessor.getDestination());
                break;

            case DISCONNECT:
                log.info("[Handler - handleMessage] DISCONNECT");
                break;
        }
    }

    private Long connectToChatRoom(MessageHeaders headers, Long userId) {
        Long roomId = getChatRoomNo(headers);
        log.info("[Handler - connectToChatRoom] 채팅방 번호 = {}", roomId);

        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();
        log.info("[Handler - connectToChatRoom] 회원 이름 = {}", user.getUserNickname());

        chatService.connectChatRoom(roomId, userId);
        log.info("[Handler - connectToChatRoom] connectChatRoom() 연결 완료");

        return roomId;
    }

    private Long getChatRoomNo(MessageHeaders headers) {
        String destination = Optional.ofNullable((String) headers.get("simpDestination")).orElse("InvalidRoomId");
        int lastIndex = destination.lastIndexOf('/');
        String roomIdStr = lastIndex != -1 ? destination.substring(lastIndex + 1) : "0";
        return Long.valueOf(roomIdStr);
    }
}
