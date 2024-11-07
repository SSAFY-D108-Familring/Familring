package com.familring.familyservice.service.chat;

import com.familring.familyservice.model.dto.Vote;
import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRepository;
import com.familring.familyservice.model.repository.VoteRepository;
import com.familring.familyservice.service.chat.event.ChatCreatedEvent;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import com.familring.familyservice.service.family.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final UserServiceFeignClient userServiceFeignClient;
    private final FamilyService familyService;
    private final ApplicationEventPublisher eventPublisher;

    private final ChatRepository chatRepository;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public Chat createChat(Long roomId, ChatRequest chatRequest) {
        log.info("[ChatService - createChat] 채팅 메시지 수신: roomId={}, senderId={}, content={}", roomId, chatRequest.getSenderId(), chatRequest.getContent());

        UserInfoResponse user = userServiceFeignClient.getUser(chatRequest.getSenderId()).getData();
        log.info("[ChatService - createChat] 회원 찾기: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());

        // 지금 시간
        LocalDateTime now = LocalDateTime.now();
        log.info("[ChatService - createChat] 지금 시간 now={}", now);

        // 채팅 객체 생성
        Chat chat = Chat.builder()
                .roomId(roomId)
                .senderId(chatRequest.getSenderId())
                .content(chatRequest.getContent())
                .createdAt(now)
                .isVote(chatRequest.isVote())
                .build();
        log.info("[ChatService - createChat] 채팅 객체 생성: chat={}", chat);

        log.info("[ChatService - createChat] chatRequest.isVote()={}", chatRequest.isVote());
        // 투표인 경우
        if(chatRequest.isVote()) {
            log.info("[ChatService - createChat] 투표인 경우");
            int familyCount = familyService.getAllFamilyCount(chatRequest.getSenderId());
            log.info("[ChatService - createChat] 가족 구성원 수: familyCount={}", familyCount);

            // 투표 객체 생성
            Vote vote = Vote.builder()
                    .voteTitle(chatRequest.getVoteTitle())
                    .voteMakerId(user.getUserId())
                    .familyCount(familyCount)
                    .isCompleted(false)
                    .createdAt(now)
                    .voteResult(new HashMap<>())
                    .choices(new HashMap<>())
                    .roomId(roomId)
                    .senderId(chatRequest.getSenderId())
                    .build();
            log.info("[ChatService - createChat] 채팅 투표 객체 생성: vote={}", vote);
            
            // 투표 정보 저장
            voteRepository.save(vote);
            log.info("[ChatService - createChat] voteRepository 저장 완료");

            // 유효한 id 넣기
            chat.setVoteId(vote.getVoteId());
            log.info("[ChatService - createChat] 생성된 투표 객체 voteId={}", chat.getVoteId());
        }
        // 투표가 아닌 경우
        else {
            // 유효하지 않은 값 넣기
            chat.setVoteId(new ObjectId());
        }
        
        // 저장
        chatRepository.save(chat);
        log.info("[ChatService - createChat] chatRepository 저장 완료");

        // ChatCreatedEvent 이벤트를 발행하여 다른 서비스나 컴포넌트에서 이 이벤트를 처리할 수 있게 함
        eventPublisher.publishEvent(new ChatCreatedEvent(roomId, chatRequest.getContent(), LocalDateTime.now().toString()));
        log.info("[ChatService - createChat] 이벤트 발행 완료");

        return chat;
    }

    @Override
    public List<ChatResponse> findAllChatByRoomId(Long roomId, Long userId) { 
        log.info("[ChatService - findAllChatByRoomId] 채팅 찾기 roomId={}, userId={}", roomId, userId);
        List<ChatResponse> responseList = new ArrayList<>();

        List<Chat> chatList = chatRepository.findAllByRoomId(roomId);
        for(Chat chat : chatList) {
            UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
            log.info("[ChatService - findAllChatByRoomId] 발신자 찾기 userNickname={}", user.getUserNickname());

            ChatResponse chatResponse = ChatResponse.builder()
                    .chatId(chat.getChatId())
                    .roomId(chat.getRoomId())
                    .senderId(chat.getSenderId())
                    .sender(user)
                    .content(chat.getContent())
                    .createdAt(chat.getCreatedAt())
                    .build();
            chatResponse.setIsVote(chat.getIsVote());

            // 투표인 경우
            if(chat.getIsVote()) {
                log.info("[ChatService - findAllChatByRoomId] 투표 찾기 voteId={}", chat.getVoteId());
                Vote vote = voteRepository.findByVoteId(chat.getVoteId()).orElse(null);
                log.info("[ChatService - findAllChatByRoomId] 찾은 투표 voteTitle={}", vote.getVoteTitle());
                chatResponse.setVote(vote);
            }
            // 투표가 아닌 경우
            else {
                log.info("[ChatService - findAllChatByRoomId] 투표가 아닌 경우 빈 객체 반환");
                chatResponse.setVote(null);
            }

            responseList.add(chatResponse);
        }

        return responseList;
    }

    @Override
    public ChatResponse findChat(Chat chat, Long userId) {
        log.info("[ChatService - toChatDTO] 채팅 정보: chat={}", chat);

        UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
        log.info("[ChatService - toChatDTO] 발신자 정보: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());
        Vote vote = null;

        if(chat.getIsVote()) {
            vote = voteRepository.findByVoteId(chat.getVoteId()).orElse(null);
            log.info("[ChatService - toChatDTO] 투표 정보: vote={}", vote);
        }

        ChatResponse response = ChatResponse.builder()
                .chatId(chat.getChatId())
                .roomId(chat.getRoomId())
                .senderId(chat.getSenderId())
                .sender(user)
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .build();
        response.setIsVote(chat.getIsVote());
        log.info("[ChatService - toChatDTO] 투표 여부: isVote={}", response.getIsVote());
        response.setVote(vote);
        log.info("[ChatService - toChatDTO] 투표 정보: vote={}", response.getVote());

        return response;
    }

    @Override
    public void connectChatRoom(Long roomId, Long userId) {
        log.info("[Service - connectChatRoom] 읽음 처리 roomId={}", roomId);
        // 나중에 읽음 처리
    }
}
