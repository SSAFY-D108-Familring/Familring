package com.familring.familyservice.service.chat;

import com.familring.familyservice.config.redis.RedisUtil;
import com.familring.familyservice.exception.chat.VoteNotFoundException;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.chat.MessageType;
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

            ChatResponse chatResponse = new ChatResponse(
                    chat.getChatId(),
                    chat.getRoomId(),
                    chat.getMessageType(),
                    chat.getSenderId(),
                    user,
                    chat.getContent(),
                    chat.getCreatedAt(),
                    null,                   // 투표 객체는 초기 설정 없음
                    chat.getResponseOfVote(),
                    chat.getIsVoteEnd(),    // 투표 종료 여부 설정
                    chat.getResultOfVote(),
                    unReadMembers           // 읽지 않은 사람 수 설정
            );

            // 투표인 경우
            if (chat.getMessageType().equals(MessageType.VOTE) || chat.getMessageType().equals(MessageType.VOTE_RESPONSE) || chat.getMessageType().equals(MessageType.VOTE_RESULT)) {
                log.info("[findPagedChatByRoomId] 투표 찾기 voteId={}", chat.getVoteId());
                Vote vote = voteRepository.findByVoteId(chat.getVoteId()).orElseThrow(() -> new VoteNotFoundException());
                log.info("[findPagedChatByRoomId] 찾은 투표 voteTitle={}", vote.getVoteTitle());
                log.info("[findPagedChatByRoomId] chatResponse.getMessageType={}", chatResponse.getMessageType());
                chatResponse.setVote(vote); // 투표 객체 설정
            }

            log.info("[findPagedChatByRoomId] chatResponse={}", chatResponse);
            responseList.add(chatResponse);
        }

        return responseList;
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
