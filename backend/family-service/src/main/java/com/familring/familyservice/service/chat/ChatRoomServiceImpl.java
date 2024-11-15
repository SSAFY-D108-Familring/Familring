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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRepository chatRepository;
    private final VoteRepository voteRepository;
    private final UserServiceFeignClient userServiceFeignClient;
    private final NotificationService notificationService;

    @Override
    public Page<ChatResponse> findPagedChatByRoomId(Long roomId, Long userId, int page, int size) {
        log.info("[findPagedChatByRoomId] 채팅 찾기 roomId={}, userId={}, page={}, size={}", roomId, userId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Chat> chatPage = chatRepository.findByRoomId(roomId, pageable);

        List<ChatResponse> responseList = chatPage.getContent().stream().map(chat -> {
            UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
            int unReadMembers = (int) (chat.getFamilyCount() - chat.getReadByUserIds().size());
            unReadMembers = Math.max(unReadMembers, 0);

            ChatResponse chatResponse = new ChatResponse(
                    chat.getChatId(),
                    chat.getRoomId(),
                    chat.getMessageType(),
                    chat.getSenderId(),
                    user,
                    chat.getContent(),
                    chat.getCreatedAt(),
                    null,
                    chat.getResponseOfVote(),
                    chat.getIsVoteEnd(),
                    chat.getResultOfVote(),
                    unReadMembers
            );

            if (chat.getMessageType().equals(MessageType.VOTE) ||
                    chat.getMessageType().equals(MessageType.VOTE_RESPONSE) ||
                    chat.getMessageType().equals(MessageType.VOTE_RESULT)) {
                Vote vote = voteRepository.findByVoteId(chat.getVoteId()).orElseThrow(VoteNotFoundException::new);
                chatResponse.setVote(vote);
            }
            return chatResponse;
        }).collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, chatPage.getTotalElements());
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
