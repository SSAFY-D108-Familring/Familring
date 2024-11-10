package com.familring.familyservice.service.chat;

import com.familring.familyservice.config.redis.RedisUtil;
import com.familring.familyservice.exception.chat.VoteNotFoundException;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.chat.Vote;
import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRepository;
import com.familring.familyservice.model.repository.VoteRepository;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomServiceImpl implements ChatRoomService {

    private final RedisUtil redisUtil;
    private final ChatRepository chatRepository;
    private final VoteRepository voteRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final NotificationService notificationService;

    @Override
    public List<ChatResponse> findPagedChatByRoomId(Long roomId, Long userId, int page, int size) {
        log.info("[findPagedChatByRoomId] 채팅 찾기 roomId={}, userId={}, page={}, size={}", roomId, userId, page, size);
        List<ChatResponse> responseList = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Chat> chatPage = chatRepository.findByRoomId(roomId, pageable);

        for (Chat chat : chatPage.getContent()) {
            UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
            log.info("[findPagedChatByRoomId] 발신자 찾기 userNickname={}", user.getUserNickname());

            // MongoDB의 readByUserIds 필드를 사용하여 읽지 않은 사람 수 계산
            int unReadMembers = (int) (chat.getFamilyCount() - chat.getReadByUserIds().size());
            unReadMembers = Math.max(unReadMembers, 0); // 0 미만일 경우 0으로 설정

            ChatResponse chatResponse = ChatResponse.builder()
                    .chatId(chat.getChatId())
                    .roomId(chat.getRoomId())
                    .senderId(chat.getSenderId())
                    .sender(user)
                    .content(chat.getContent())
                    .createdAt(chat.getCreatedAt())
                    .isVote(chat.getIsVote())
                    .vote(null)
                    .isVoteResponse(chat.getIsVoteResponse())
                    .responseOfVote(chat.getResponseOfVote())
                    .isVoteResult(chat.getIsVoteResult())
                    .resultOfVote(chat.getResultOfVote())
                    .unReadMembers(unReadMembers) // 읽지 않은 사람 수 설정
                    .build();

            // 투표인 경우
            if (chat.getIsVote()) {
                log.info("[findPagedChatByRoomId] 투표 찾기 voteId={}", chat.getVoteId());
                Vote vote = voteRepository.findByVoteId(chat.getVoteId()).orElseThrow(() -> new VoteNotFoundException());
                log.info("[findPagedChatByRoomId] 찾은 투표 voteTitle={}", vote.getVoteTitle());
                chatResponse.setIsVote(chat.getIsVote());
                log.info("[findPagedChatByRoomId] chatResponse.isVote={}", chatResponse.getIsVote());
                chatResponse.setVote(vote);
            }

            log.info("[findPagedChatByRoomId] chatResponse={}", chatResponse);
            responseList.add(chatResponse);
        }

        return responseList;
    }

    @Override
    public void connectChatRoom(Long roomId, Long userId) {
        log.info("[connectChatRoom] roomId={}, userId={}", roomId, userId);

        String chatRoomUserCountKey = "CHAT_ROOM_USER_COUNT_" + roomId;
        log.info("[connectChatRoom] chatRoomUserCountKey={}", chatRoomUserCountKey);

        // 채팅방에 있는 사용자를 Redis에 추가
        Long roomUserCount = redisUtil.insertSet(chatRoomUserCountKey, String.valueOf(userId));
        log.info("[connectChatRoom] 구독 후 채팅방 사람 수 roomUserCount={}", roomUserCount);

        // 읽음 처리
        markMessagesAsRead(roomId, userId);
        log.info("[connectChatRoom] 읽음 처리 완료");

        // 새로운 사용자가 입장하면 비동기로 읽음 수를 업데이트하는 이벤트 전송
        notifyReadStatusUpdate(roomId);
        log.info("[connectChatRoom] 이벤트 전송 완료");
    }

    @Override
    public void disconnectChatRoom(Long roomId, Long userId) {
        log.info("[disconnectChatRoom] roomId={}, userId={}", roomId, userId);

        String chatRoomUserCountKey = "CHAT_ROOM_USER_COUNT_" + roomId;

        // Redis에서 사용자 퇴장 처리
        Long remainingUserCount = redisUtil.deleteSet(chatRoomUserCountKey, String.valueOf(userId));
        log.info("[disconnectChatRoom] 퇴장 후 채팅방 인원 수 remainingUserCount={}", remainingUserCount);

        // 읽음 상태 업데이트 알림 전송
        notifyRoomExit(roomId, userId);
    }

    private void markMessagesAsRead(Long roomId, Long userId) {
        List<Chat> chats = chatRepository.findAllByRoomId(roomId);

        for (Chat chat : chats) {
            String readStatusKey = "READ_STATUS_" + roomId + "_" + chat.getChatId();
            String unreadCountKey = "UNREAD_COUNT_" + roomId + "_" + chat.getChatId();

            // Redis에 읽음 사용자 추가 및 읽지 않은 사람 수 감소
            if (redisUtil.insertSet(readStatusKey, String.valueOf(userId)) == 1) { // 새로 읽은 경우
                redisUtil.decrementString(unreadCountKey); // 읽지 않은 사람 수 감소
                log.info("[markMessagesAsRead] userId={}가 messageId={}를 Redis에서 읽음 처리함", userId, chat.getChatId());
            }

            // MongoDB에 읽음 업데이트
            chat.markAsRead(userId);
            chatRepository.save(chat);
        }
    }

    @Override
    public void notifyReadStatusUpdate(Long roomId) {
        notificationService.notifyReadStatusUpdate(roomId);
    }

    @Override
    public void notifyRoomExit(Long roomId, Long userId) {
        notificationService.notifyRoomExit(roomId, userId);
    }
}
