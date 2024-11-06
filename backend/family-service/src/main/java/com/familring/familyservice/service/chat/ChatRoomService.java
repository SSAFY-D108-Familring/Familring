package com.familring.familyservice.service.chat;

import com.familring.familyservice.config.redis.RedisUtils;
import com.familring.familyservice.exception.chat.ChatRoomNotFoundException;
import com.familring.familyservice.model.dto.chat.ChatRoom;
import com.familring.familyservice.model.dto.chat.ChatRoomUser;
import com.familring.familyservice.model.dto.request.CreateChatRoomRequest;
import com.familring.familyservice.model.dto.response.CreateChatRoomResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRoomRepository;
import com.familring.familyservice.model.repository.ChatRoomUserRepository;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ChatRoomService {

    private final UserServiceFeignClient userServiceClient;
    private final RedisUtils redisUtil;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    // 새로운 채팅방을 생성
    public CreateChatRoomResponse createChatRoom(CreateChatRoomRequest dto) {
        // 로그인 한 회원 정보 요청
        UserInfoResponse loginUser = userServiceClient.getUser(dto.getUserId()).getData();
        log.debug("[채팅방 생성 Service] 로그인 유저 = {}", loginUser.getUserNickname());

        // 채팅방 생성
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder().build());
        // 채팅방 유저 생성
        ChatRoomUser loginUserRoom = createChatRoomUser(loginUser.getUserId(), chatRoom);

        chatRoomUserRepository.save(loginUserRoom);

        return CreateChatRoomResponse.builder()
                .roomId(chatRoom.getId())
                .build();
    }

    // 채팅방 회원 Entity 생성
    private ChatRoomUser createChatRoomUser(Long userId, ChatRoom chatRoom) {
        return ChatRoomUser.builder()
                .chatRoomTitle(generateChatRoomTitle(opponentUser, schoolClassId))
                .childName(childName)
                .userId(userId)
                .opponentUserType(String.valueOf(opponentUser.getUserType()))
                .isAlarm(true)
                .unreadCount(0)
                .chatRoom(chatRoom)
                .build();
    }

    // 채팅방 입장시 채팅방에 있는 인원 증가
    public void connectChatRoom(Long chatRoomId, Long userId) {
        log.debug("[소켓 연결] 레디스. 채팅방 번호 = {}", chatRoomId);
        String chatRoomUserCountKey = "CHAT_ROOM_USER_COUNT_" + chatRoomId;
        Long roomUserCount = redisUtil.getSetSize(chatRoomUserCountKey);
        log.debug("[소켓 연결] 기존 채팅방 구독한 사람 수 = {}", roomUserCount);
        roomUserCount = redisUtil.insertSet(chatRoomUserCountKey, String.valueOf(userId));
        log.debug("[소켓 연결] 구독 후 채팅방 사람 수 = {}", roomUserCount);

        //채팅 읽음 처리
        ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
                .orElseThrow(() -> new ChatRoomNotFoundException());
        chatRoomUser.read();
    }
}
