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
import com.familring.familyservice.service.chat.event.ChatRoomConnectEvent;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import com.familring.familyservice.service.family.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final ApplicationEventPublisher eventPublisher;
    private final UserServiceFeignClient userServiceFeignClient;
    private final FamilyService familyService;

    private final ChatRepository chatRepository;
    private final VoteRepository voteRepository;

    @EventListener
    public void handleChatRoomConnect(ChatRoomConnectEvent event) {
        connectChatRoom(event.getRoomId(), event.getUserId());
    }

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
                .isVoteEnd(false)
                .responseOfVote("") // 투표 응답 유효하지 않은 값 넣기
                .isVoteResult(false)
                .resultOfVote(new HashMap<>()) // 투표 결과 유효하지 않은 값 넣기
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

        // chatRepository에 저장
        chatRepository.save(chat);
        log.info("[createChat] chatRepository 저장 완료");

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
        if (vote.getChoices().containsKey(participantId)) {
            log.info("[createChatVoteResponse] 투표자Id={}가 이미 투표에 참여했습니다.", participantId);
            throw new AlreadyVoteParticipantException();
        }
        log.info("[createChatVoteResponse] 투표자Id={}, 응답={}", participantId, voteResponse);
        vote.getChoices().put(participantId, voteResponse);

        // 투표 저장
        voteRepository.save(vote);
        log.info("[createChatVoteResponse] voteRepository 저장 완료");

        // 채팅 객체 생성
        Chat voteChat = Chat.builder()
                .roomId(roomId)
                .senderId(chatRequest.getSenderId())
                .content(chatRequest.getContent())
                .createdAt(now)
                .isVote(chatRequest.isVote())
                .voteId(voteId)
                .isVoteResponse(chatRequest.getIsVoteResponse())
                .responseOfVote(chatRequest.getResponseOfVote())
                .isVoteResult(false)
                .resultOfVote(new HashMap<>())
                .build();
        log.info("[createChatVoteResponse] 투표 응답 채팅 객체: voteChat={}", voteChat);

        // 투표를 모두 참여한 경우 체크
        if(vote.getFamilyCount() == vote.getChoices().size()) {
            log.info("[createChatVoteResponse] 모든 사용자 투표 참여 완료: familyCount={}, participants={}", vote.getFamilyCount(), vote.getChoices().size());
            Map<Long, String> choices = vote.getChoices();
            Map<String, Integer> voteResult = new HashMap<>();

            // 투표 참여자들 결과 카운트하기
            for (String choice : choices.values()) {
                voteResult.put(choice, voteResult.getOrDefault(choice, 0) + 1);
            }

            log.info("[createChatVoteResponse] 투표 객체에 voteResult 넣기");
            vote.setCompleted(true);
            vote.setVoteResult(voteResult);
            voteRepository.save(vote);

            log.info("[createChatVoteResponse] 채팅 객체에 resultOfVote 넣기");
            voteChat.setVoteEnd(true);
        }

        // 투표 응답 채팅 객체 저장
        chatRepository.save(voteChat);
        log.info("[createChatVoteResponse] 투표 응답 채팅 chatRepository 저장 완료");

        return voteChat;
    }

    @Override
    public Chat createChatVoteResult(Long roomId, String voteId, ChatRequest chatRequest) {
        // 투표 찾기
        Vote vote = voteRepository.findByVoteId(voteId)
                .orElseThrow(() -> new VoteNotFoundException());
        log.info("[createChatVoteResult] 투표 객체 찾기: vote={}", vote);

        // 투표 결과 채팅 객체 생성
        Chat voteResultChat = Chat.builder()
                .roomId(roomId)
                .senderId(vote.getSenderId())
                .content(vote.getVoteTitle())
                .createdAt(LocalDateTime.now())
                .isVote(false)
                .voteId(voteId)
                .isVoteResponse(false)
                .responseOfVote("")
                .isVoteResult(true)
                .resultOfVote(vote.getVoteResult())
                .build();

        // 투표 결과 채팅 저장
        chatRepository.save(voteResultChat);
        log.info("[handleVoteCompleted] 투표 결과 채팅 저장 완료: chatId={}", voteResultChat.getChatId());

        return voteResultChat;
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
                    .isVoteResult(chat.getIsVoteResult())
                    .resultOfVote(chat.getResultOfVote())
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
    public List<ChatResponse> findPagedChatByRoomId(Long roomId, Long userId, int page, int size) {
        log.info("[findPagedChatByRoomId] 채팅 찾기 roomId={}, userId={}, page={}, size={}", roomId, userId, page, size);
        List<ChatResponse> responseList = new ArrayList<>();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Chat> chatPage = chatRepository.findByRoomId(roomId, pageable);

        for (Chat chat : chatPage.getContent()) {
            UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
            log.info("[findPagedChatByRoomId] 발신자 찾기 userNickname={}", user.getUserNickname());

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
                    .build();

            if (chat.getIsVote()) {
                log.info("[findPagedChatByRoomId] 투표 찾기 voteId={}", chat.getVoteId());
                Vote vote = voteRepository.findByVoteId(chat.getVoteId()).orElseThrow(() -> new VoteNotFoundException());
                log.info("[findPagedChatByRoomId] 찾은 투표 voteTitle={}", vote.getVoteTitle());
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
                .isVoteResult(chat.getIsVoteResult())
                .resultOfVote(chat.getResultOfVote())
                .build();

        return response;
    }

    @Override
    public void connectChatRoom(Long roomId, Long userId) {
        log.info("[Service - connectChatRoom] 읽음 처리 roomId={}", roomId);
        // 나중에 읽음 처리
    }
}
