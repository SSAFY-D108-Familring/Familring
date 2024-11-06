package com.familring.familyservice.config.websocket;

import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.chat.ChatRoomService;
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

    private final ChatRoomService chatRoomService;
    private final UserServiceFeignClient userServiceClient;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 메시지 전송 전 메시지 정보 추출
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        handleMessage(accessor.getCommand(), accessor, message.getHeaders());

        return message;
    }

    private void handleMessage(StompCommand command, StompHeaderAccessor accessor, MessageHeaders headers) {
        // StompCommand에 따라 연결, 구독, 전송, 해제 수행
        switch (command) {
            case CONNECT:
                log.info("[소켓] CONNECT");
                break;
            case SUBSCRIBE: // 구독인 경우 채팅방 연결 시도
                log.info("[소켓] SUBSCRIBE");
                log.info("[소켓] 채팅방 구독 시도");
                Long chatRoomId = connectToChatRoom(accessor, headers);
                log.info("[소켓] 채팅방 구독한 채팅 방 = {}", chatRoomId);
                break;
            case SEND:
                log.info("[소켓] SEND");
                log.info("[소켓] 메세지 맵핑 주소 = {}", accessor.getDestination());
                break;
            case DISCONNECT:
                log.info("[소켓] DISCONNECT");
                break;
        }
    }

    // 채팅방 번호와 사용자 ID를 통해 사용자를 채팅방에 연결
    private Long connectToChatRoom(StompHeaderAccessor accessor, MessageHeaders headers) {
        // 채팅방 ID를 추출
        Long chatRoomId = getChatRoomNo(headers);
        log.info("[소켓 연결] 채팅방 ID = {}", chatRoomId);
        // STOMP 헤더에서 사용자 ID 추출
        Long userId = Long.parseLong(accessor.getFirstNativeHeader("X-User-ID"));
        log.info("[소켓 연결] 사용자 ID = {}", userId);
        // feignClient를 사용해 사용자 정보 조회
        UserInfoResponse userInfo = userServiceClient.getUser(userId).getData();
        log.info("[소켓 연결] 사용자 NickName = {}", userInfo.getUserNickname());
        // 채팅방에 사용자 연결
        chatRoomService.connectChatRoom(chatRoomId, userInfo.getUserId());
        log.info("[소켓 연결] 채팅방 연결 완료");

        return chatRoomId;
    }

    // destination 경로에서 채팅방 ID를 추출
    private String getRoomId(String destination) {
        // 경로의 마지막 슬래시(/) 이후의 문자열을 채팅방 ID로 간주하여 반환
        int lastIndex = destination.lastIndexOf('/');

        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        } else {
            return null;
        }
    }

    // 메시지 헤더에서 simpDestination을 가져와 채팅방 번호를 추출
    private Long getChatRoomNo(MessageHeaders headers) {
        String roomId = getRoomId(Optional.ofNullable((String)
                headers.get("simpDestination")).orElse("InvalidRoomId"));

        return Long.valueOf(roomId);
    }
}
