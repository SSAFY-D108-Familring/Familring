package com.familring.familyservice.config.websocket;

import com.familring.familyservice.config.redis.RedisUtil;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRepository;
import com.familring.familyservice.service.chat.ChatRoomService;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class StompHandler implements ChannelInterceptor {

    private final UserServiceFeignClient userServiceFeignClient;
    private final RedisUtil redisUtil;
    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final MongoTemplate mongoTemplate;

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
                log.info("[handleMessage] CONNECT");
                String userIdHeader = accessor.getFirstNativeHeader("X-User-ID");
                log.info("[handleMessage] WebSocket 연결 시 X-User-ID 헤더: {}", userIdHeader);

                if (userIdHeader == null) {
                    log.error("X-User-ID 헤더가 누락되었습니다.");
                    throw new IllegalArgumentException("X-User-ID 헤더가 필요합니다.");
                }

                Long userId = Long.valueOf(userIdHeader);
                accessor.getSessionAttributes().put("userId", userId); // 세션에 저장
                break;

            case SUBSCRIBE:
                log.info("[handleMessage] SUBSCRIBE");
                String destination = accessor.getDestination();
                userId = (Long) accessor.getSessionAttributes().get("userId");
                Long connectRoomId = getChatRoomNo(headers);

                if (destination.equals("/room/" + connectRoomId)) {
                    Long roomId = connectToChatRoom(headers, userId);
                    log.info("[handleMessage] 구독한 채팅 방 = {}", roomId);
                    log.info("[handleMessage] SUBSCRIBE 시 읽음 처리 완료 for userId={} in roomId={}", userId, roomId);
                } else {
                    log.info("[handleMessage] 특정 기능을 위한 구독 경로: {}", destination);
                }
                break;

            case SEND:
                log.info("[handleMessage] SEND");
                destination = accessor.getDestination();
                log.info("[handleMessage] 메세지 맵핑 주소 = {}", destination);

                // 목적지가 /app/{roomId}/vote 패턴과 일치하는지 확인
                if (destination != null && destination.matches("^/app/\\d+/vote$")) {
                    log.info("[handleMessage] 유효한 투표 경로로 메시지가 전송되었습니다.");
                } else {
                    log.warn("[handleMessage] 유효하지 않은 경로로 메시지가 전송되었습니다: {}", destination);
                }
                break;

            case DISCONNECT:
                log.info("[handleMessage] DISCONNECT");
                userId = (Long) accessor.getSessionAttributes().get("userId");
                if (userId != null) {
                    Long disconnectRoomId = getChatRoomNo(headers);
                    if (disconnectRoomId != null) {
                        log.info("[handleMessage] userId={}가 roomId={}에서 퇴장합니다.", userId, disconnectRoomId);

                        // Redis에서 채팅방 인원 수 감소 처리
                        disconnectChatRoom(disconnectRoomId, userId);
                    } else {
                        log.warn("[handleMessage] 퇴장하려는 채팅방 번호가 유효하지 않습니다.");
                    }
                } else {
                    log.warn("[handleMessage] 세션에 userId가 없습니다.");
                }
                break;
        }
    }

    public void connectChatRoom(Long roomId, Long userId) {
        log.info("[connectChatRoom] roomId={}, userId={}", roomId, userId);

        String chatRoomUserCountKey = "CHAT_ROOM_USER_COUNT_" + roomId;
        log.info("[connectChatRoom] chatRoomUserCountKey={}", chatRoomUserCountKey);

        Long roomUserCount = redisUtil.getSetSize(chatRoomUserCountKey);
        log.info("[connectChatRoom] 기존 채팅방 구독한 사람 수 roomUserCount={}", roomUserCount);

        // 채팅방에 있는 사용자를 Redis에 추가
        roomUserCount = redisUtil.insertSet(chatRoomUserCountKey, String.valueOf(userId));
        log.info("[connectChatRoom] 구독 후 채팅방 사람 수 roomUserCount={}", roomUserCount);

        // CompletableFuture로 읽음 처리 후 이벤트 호출
        CompletableFuture.runAsync(() -> markMessagesAsRead(roomId, userId))
                .thenRunAsync(() -> chatRoomService.notifyReadStatusUpdate(roomId));

        log.info("[connectChatRoom] 읽음 처리 완료 후 이벤트 전송 완료");
    }

    public void disconnectChatRoom(Long roomId, Long userId) {
        log.info("[disconnectChatRoom] roomId={}, userId={}", roomId, userId);

        String chatRoomUserCountKey = "CHAT_ROOM_USER_COUNT_" + roomId;

        // Redis에서 사용자 퇴장 처리
        Long remainingUserCount = redisUtil.deleteSet(chatRoomUserCountKey, String.valueOf(userId));
        log.info("[disconnectChatRoom] 퇴장 후 채팅방 인원 수 remainingUserCount={}", remainingUserCount);

        // 읽음 상태 업데이트 알림 전송
        chatRoomService.notifyRoomExit(roomId, userId);
    }

    private Long connectToChatRoom(MessageHeaders headers, Long userId) {
        Long roomId = getChatRoomNo(headers);
        log.info("[connectToChatRoom] 채팅방 번호 = {}", roomId);

        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();
        log.info("[connectToChatRoom] 회원 이름 = {}", user.getUserNickname());

        connectChatRoom(roomId, user.getUserId());
        log.info("[connectToChatRoom] 채팅방 인원 증가");

        return roomId;
    }

    private Long getChatRoomNo(MessageHeaders headers) {
        String destination = Optional.ofNullable((String) headers.get("simpDestination")).orElse("InvalidRoomId");
        // 경로가 유효한 경우에만 roomId를 파싱
        if (destination.startsWith("/room/")) {
            String[] pathSegments = destination.split("/");
            if (pathSegments.length > 2) {
                try {
                    return Long.valueOf(pathSegments[2]);
                } catch (NumberFormatException e) {
                    log.warn("[getChatRoomNo] roomId 파싱 중 오류 발생: " + pathSegments[2], e);
                }
            }
        }
        log.warn("[getChatRoomNo] 유효하지 않은 경로 형식: " + destination);
        return null;
    }

    private void markMessagesAsRead(Long roomId, Long userId) {
        List<Chat> chats = chatRepository.findAllByRoomId(roomId);

        for (Chat chat : chats) {
            String readStatusKey = "READ_STATUS_" + roomId + "_" + chat.getChatId();
            String unreadCountKey = "UNREAD_COUNT_" + roomId + "_" + chat.getChatId();

            // Redis에서 읽음 상태 체크
            boolean isAlreadyRead = redisUtil.containSet(readStatusKey, String.valueOf(userId));

            // MongoDB에서 읽음 상태 체크
            boolean isAlreadyReadInMongo = chat.getReadByUserIds().contains(userId);

            if (!isAlreadyRead && !isAlreadyReadInMongo) {
                redisUtil.insertSet(readStatusKey, String.valueOf(userId));
                redisUtil.decrementString(unreadCountKey);
                log.info("[markMessagesAsRead] userId={}가 messageId={}를 Redis에서 읽음 처리함", userId, chat.getChatId());

                chat.markAsRead(userId);
                chatRepository.save(chat);
                log.info("[markMessagesAsRead] userId={}가 messageId={}를 MongoDB에서 읽음 처리함", userId, chat.getChatId());
            }
        }
    }

}
