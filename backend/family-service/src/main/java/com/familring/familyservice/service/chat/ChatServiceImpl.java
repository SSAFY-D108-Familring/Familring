package com.familring.familyservice.service.chat;

import com.familring.familyservice.exception.chat.AlreadyVoteParticipantException;
import com.familring.familyservice.exception.chat.VoteNotFoundException;
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
    public Chat createChatAndVote(Long roomId, ChatRequest chatRequest) {
        log.info("[createChat] 채팅 메시지 수신: roomId={}, senderId={}, content={}", roomId, chatRequest.getSenderId(), chatRequest.getContent());

        UserInfoResponse user = userServiceFeignClient.getUser(chatRequest.getSenderId()).getData();
        log.info("[createChat] 회원 찾기: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());

        // 지금 시간
        LocalDateTime now = LocalDateTime.now();
        log.info("[createChat] 지금 시간 now={}", now);

        // 채팅 객체 생성
        Chat chat = Chat.builder()
                .roomId(roomId)
                .senderId(chatRequest.getSenderId())
                .content(chatRequest.getContent())
                .createdAt(now)
                .isVote(chatRequest.isVote())
                .voteId("") // 투표 유효하지 않은 값 넣기
                .isVoteResponse(chatRequest.getIsVoteResponse())
                .responseOfVote("") // 투표 응답 유효하지 않은 값 넣기
                .build();
        log.info("[createChat] 채팅 객체 생성: chat={}", chat);
        log.info("[createChat] chatRequest.isVote()={}", chatRequest.isVote());

        if(chatRequest.isVote()) {
            log.info("[createChat] 투표인 경우");
            int familyCount = familyService.getAllFamilyCount(chatRequest.getSenderId());
            log.info("[createChat] 가족 구성원 수: familyCount={}", familyCount);

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
            log.info("[createChat] 채팅 투표 객체 생성: vote={}", vote);
            
            // 투표 정보 저장
            voteRepository.save(vote);
            log.info("[createChat] voteRepository 저장 완료");

            // 유효한 id 넣기
            chat.setVoteId(vote.getVoteId());
            log.info("[createChat] 생성된 투표 객체 voteId={}", chat.getVoteId());
        }
        
        // 저장
        chatRepository.save(chat);
        log.info("[createChat] chatRepository 저장 완료");

        // ChatCreatedEvent 이벤트를 발행하여 다른 서비스나 컴포넌트에서 이 이벤트를 처리할 수 있게 함
        eventPublisher.publishEvent(new ChatCreatedEvent(roomId, chatRequest.getContent(), LocalDateTime.now().toString()));
        log.info("[createChat] 이벤트 발행 완료");

        return chat;
    }

    @Override
    @Transactional
    public Chat createChatVoteResponse(Long roomId, String voteId, ChatRequest chatRequest) {
        log.info("[createChatVoteResponse] 투표 메시지 수신: roomId={}, voteId={}, senderId={}", roomId, voteId, chatRequest.getSenderId());

        UserInfoResponse user = userServiceFeignClient.getUser(chatRequest.getSenderId()).getData();
        log.info("[createChatVoteResponse] 회원 찾기: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());

        LocalDateTime now = LocalDateTime.now();
        log.info("[createChatVoteResponse] 지금 시간 now={}", now);

        // 투표 찾기
        Vote vote = voteRepository.findByVoteId(voteId)
                .orElseThrow(() -> new VoteNotFoundException());
        log.info("[createChatVoteResponse] 투표 객체 찾기: vote={}", vote);

        // 투표 참여
        Long participantId = user.getUserId(); // 투표자
        String voteResponse = chatRequest.getResponseOfVote();
        // 이미 투표 진행했는지 확인
        if (vote.getChoices().containsKey(participantId)) {
            log.info("[createChatVoteResponse] 투표자Id={}가 이미 투표에 참여했습니다.", participantId);
            throw new AlreadyVoteParticipantException();
        }
        log.info("[createChatVoteResponse] 투표자Id={}, 응답={}", participantId, voteResponse);
        vote.getChoices().put(participantId, voteResponse);

        // 투표 결과 반영
        Integer count = vote.getVoteResult().get(chatRequest.getResponseOfVote()); // responseOfVote를 정확히 사용
        if (count == null) {
            count = 0; // 기본값을 0으로 설정
        }
        log.info("[createChatVoteResponse] 투표 전 응답={}, 응답한 사람 수={}", chatRequest.getResponseOfVote(), count);
        vote.getVoteResult().put(chatRequest.getResponseOfVote(), count + 1);
        log.info("[createChatVoteResponse] 투표 후 응답={}, 응답한 사람 수={}", chatRequest.getResponseOfVote(), count + 1);

        // 투표 저장
        voteRepository.save(vote);

        // 투표를 모두 참여한 경우 체크
        if(vote.getFamilyCount() == vote.getChoices().size()) {
            log.info("[createChatVoteResponse] 모든 사용자 투표 참여 완료: familyCount={}, participants={}", vote.getFamilyCount(), vote.getChoices().size());
            log.info("[createChatVoteResponse] voteResult={}", vote.getVoteResult());
        }

        // 채팅 객체 생성
        Chat chat = Chat.builder()
                .roomId(roomId)
                .senderId(chatRequest.getSenderId())
                .content(chatRequest.getContent())
                .createdAt(now)
                .isVote(chatRequest.isVote())
                .voteId(voteId)
                .isVoteResponse(chatRequest.getIsVoteResponse())
                .responseOfVote(chatRequest.getResponseOfVote())
                .build();
        log.info("[createChatVoteResponse] 채팅 객체 생성: chat={}", chat);

        return chat;
    }

    @Override
    public List<ChatResponse> findAllChatByRoomId(Long roomId, Long userId) { 
        log.info("[findAllChatByRoomId] 채팅 찾기 roomId={}, userId={}", roomId, userId);
        List<ChatResponse> responseList = new ArrayList<>();

        List<Chat> chatList = chatRepository.findAllByRoomId(roomId);
        for(Chat chat : chatList) {
            UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
            log.info("[findAllChatByRoomId] 발신자 찾기 userNickname={}", user.getUserNickname());

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
                    .build();

            // 투표인 경우
            if(chat.getIsVote()) {
                log.info("[findAllChatByRoomId] 투표 찾기 voteId={}", chat.getVoteId());
                Vote vote = voteRepository.findByVoteId(chat.getVoteId())
                        .orElseThrow(() -> new VoteNotFoundException());
                log.info("[findAllChatByRoomId] 찾은 투표 voteTitle={}", vote.getVoteTitle());
                chatResponse.setVote(vote);
            }

            responseList.add(chatResponse);
        }

        return responseList;
    }

    @Override
    public ChatResponse findChat(Chat chat, Long userId) {
        log.info("[findChat] 채팅 정보: chat={}", chat);

        UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
        log.info("[findChat] 발신자 정보: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());
        Vote vote = null;

        if(chat.getIsVote()) {
            vote = voteRepository.findByVoteId(chat.getVoteId()).orElse(null);
            log.info("[findChat] 투표 정보: vote={}", vote);
        }

        ChatResponse response = ChatResponse.builder()
                .chatId(chat.getChatId())
                .roomId(chat.getRoomId())
                .senderId(chat.getSenderId())
                .sender(user)
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .isVote(chat.getIsVote())
                .vote(vote)
                .isVoteResponse(chat.getIsVoteResponse())
                .responseOfVote(chat.getResponseOfVote())
                .build();

        return response;
    }

    @Override
    public void connectChatRoom(Long roomId, Long userId) {
        log.info("[Service - connectChatRoom] 읽음 처리 roomId={}", roomId);
        // 나중에 읽음 처리
    }
}
