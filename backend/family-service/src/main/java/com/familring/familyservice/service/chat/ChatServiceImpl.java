package com.familring.familyservice.service.chat;

import com.familring.familyservice.config.redis.RedisUtil;
import com.familring.familyservice.exception.chat.AlreadyVoteParticipantException;
import com.familring.familyservice.exception.chat.VoteNotFoundException;
import com.familring.familyservice.model.dto.chat.MessageType;
import com.familring.familyservice.model.dto.chat.Vote;
import com.familring.familyservice.model.dto.response.ChatResponse;
import com.familring.familyservice.model.dto.chat.Chat;
import com.familring.familyservice.model.dto.request.ChatRequest;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.model.repository.ChatRepository;
import com.familring.familyservice.model.repository.VoteRepository;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import com.familring.familyservice.service.family.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final RedisUtil redisUtil;

    private final UserServiceFeignClient userServiceFeignClient;

    private final FamilyService familyService;
    private final ChatRoomService chatRoomService;

    private final ChatRepository chatRepository;
    private final VoteRepository voteRepository;

    @Override
    @Transactional
    public Chat createChatOrVoiceOrVote(Long roomId, ChatRequest chatRequest) {
        log.info("[createChatAndVote] 채팅 메시지 수신: roomId={}, senderId={}, messageType={}, content={}", roomId, chatRequest.getSenderId(), chatRequest.getMessageType(), chatRequest.getContent());

        UserInfoResponse user = userServiceFeignClient.getUser(chatRequest.getSenderId()).getData();
        log.info("[createChatAndVote] 회원 찾기: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());

        int familyCount = familyService.getAllFamilyCount(chatRequest.getSenderId());
        log.info("[createChatAndVote] 가족 구성원 수: familyCount={}", familyCount);

        LocalDateTime now = LocalDateTime.now();
        log.info("[createChatAndVote] 지금 시간 now={}", now);

        // 채팅방에 구독 중인 사용자들을 모두 읽음 처리
        String chatRoomUserKey = "CHAT_ROOM_USER_COUNT_" + roomId;
        Set<String> currentUserIds = redisUtil.getSetMembers(chatRoomUserKey);
        Set<Long> readByUserIds = currentUserIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        // 채팅 객체 생성
        Chat chat = Chat.builder()
                .roomId(roomId)
                .messageType(chatRequest.getMessageType())
                .familyCount(familyCount)
                .senderId(chatRequest.getSenderId())
                .content(chatRequest.getContent())
                .createdAt(now)
                .voteId("") // 유효하지 않은 값
                .isVoteEnd(false)
                .responseOfVote("") // 유효하지 않은 값
                .resultOfVote(new HashMap<>())
                .readByUserIds(readByUserIds) // 모든 구독 중인 사용자 읽음 처리
                .build();

        log.info("[createChatAndVote] 채팅 객체 생성: chat={}", chat);

        // 읽지 않은 사용자 수 계산
        String unreadCountKey = "UNREAD_COUNT_" + roomId + "_" + chat.getChatId();
        redisUtil.setString(unreadCountKey, String.valueOf(familyCount - readByUserIds.size()));

        if(chatRequest.getMessageType().equals(MessageType.VOTE)) {
            // 투표 객체 생성
            Vote vote = Vote.builder()
                    .voteTitle(chatRequest.getVoteTitle())
                    .voteMakerId(user.getUserId())
                    .familyCount(familyCount)
                    .isCompleted(false)
                    .createdAt(now)
                    .voteResult(new HashMap<>())
                    .choices(new HashMap<>() {{
                        put(user.getUserId(), "agree"); // 투표 생성자를 찬성으로 등록
                    }})
                    .roomId(roomId)
                    .senderId(chatRequest.getSenderId())
                    .build();
            log.info("[createChatAndVote] 채팅 투표 객체 생성: vote={}", vote);

            // 투표 정보 저장
            voteRepository.save(vote);
            log.info("[createChatAndVote] voteRepository 저장 완료");

            chat.setVoteId(vote.getVoteId());
            log.info("[createChatAndVote] 생성된 투표 객체 voteId={}", chat.getVoteId());
        }

        chatRepository.save(chat);
        log.info("[createChatAndVote] chatRepository 저장 완료");

        // 읽음 상태 업데이트 이벤트 전송
        chatRoomService.notifyReadStatusUpdate(roomId);

        return chat;
    }

    @Override
    @Transactional
    public Chat createChatVoteResponse(Long roomId, String voteId, ChatRequest chatRequest) {
        log.info("[createChatVoteResponse] 투표 메시지 수신: roomId={}, voteId={}, senderId={}", roomId, voteId, chatRequest.getSenderId());

        UserInfoResponse user = userServiceFeignClient.getUser(chatRequest.getSenderId()).getData();
        log.info("[createChatVoteResponse] 회원 찾기: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());

        int familyCount = familyService.getAllFamilyCount(chatRequest.getSenderId());
        log.info("[createChatVoteResponse] 가족 구성원 수: familyCount={}", familyCount);

        LocalDateTime now = LocalDateTime.now();
        log.info("[createChatVoteResponse] 지금 시간 now={}", now);

        // 투표 찾기
        Vote vote = voteRepository.findByVoteId(voteId)
                .orElseThrow(() -> new VoteNotFoundException());
        log.info("[createChatVoteResponse] 투표 객체 찾기: vote={}", vote);

        // 투표 참여
        Long participantId = user.getUserId();
        String voteResponse = chatRequest.getResponseOfVote();
        if (vote.getChoices().containsKey(participantId)) {
            log.info("[createChatVoteResponse] 해당 인원이 이미 투표에 참여 완료");
            throw new AlreadyVoteParticipantException();
        }
        vote.getChoices().put(participantId, voteResponse);
        voteRepository.save(vote);

        // 채팅방에 구독 중인 사용자들을 모두 읽음 처리
        String chatRoomUserKey = "CHAT_ROOM_USER_COUNT_" + roomId;
        Set<String> currentUserIds = redisUtil.getSetMembers(chatRoomUserKey);
        Set<Long> readByUserIds = currentUserIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        log.info("[createChatVoteResponse] 읽음 처리 완료 readByUserIds={}", readByUserIds.toArray(new Long[0]));

        // 투표 응답 채팅 객체 생성
        Chat voteChat = Chat.builder()
                .roomId(roomId)
                .messageType(MessageType.VOTE_RESPONSE)
                .senderId(chatRequest.getSenderId())
                .familyCount(familyCount)
                .content(chatRequest.getContent())
                .createdAt(now)
                .voteId(voteId)
                .responseOfVote(chatRequest.getResponseOfVote())
                .resultOfVote(new HashMap<>())
                .readByUserIds(readByUserIds) // 모든 구독 중인 사용자 읽음 처리
                .build();
        log.info("[createChatVoteResponse] chat 객체 Message Type={}", voteChat.getMessageType());

        // 읽지 않은 사용자 수를 Redis에 저장
        String unreadCountKey = "UNREAD_COUNT_" + roomId + "_" + voteChat.getChatId();
        redisUtil.setString(unreadCountKey, String.valueOf(familyCount - readByUserIds.size()));

        // 투표 결과 체크 및 저장
        if (vote.getFamilyCount() == vote.getChoices().size()) {
            log.info("[createChatVoteResponse] 가족 구성원 모두 투표 참여 완료");
            vote.setCompleted(true); // 투표 완료 처리
            vote.setVoteResult(vote.getChoices().values().stream()
                    .collect(Collectors.groupingBy(choice -> choice, Collectors.summingInt(v -> 1))));
            voteRepository.save(vote); // 투표 저장

            voteChat.setIsVoteEnd(true); // 투표 응답에 투표 끝났다고 저장
            log.info("[createChatVoteResponse] 투표 끝났다고 저장 완료={}", voteChat.getIsVoteEnd());
        }

        chatRepository.save(voteChat); // 채팅 저장
        log.info("[createChatVoteResponse] 저장된 채팅 정보 voteChat={}", voteChat);

        chatRoomService.notifyReadStatusUpdate(roomId);

        return voteChat;
    }

    @Override
    @Transactional
    public Chat createChatVoteResult(Long roomId, String voteId, ChatRequest chatRequest) {
        Vote vote = voteRepository.findByVoteId(voteId)
                .orElseThrow(() -> new VoteNotFoundException());
        log.info("[createChatVoteResult] 종료된 투표 정보 vote={}", vote);

        int familyCount = familyService.getAllFamilyCount(chatRequest.getSenderId());
        log.info("[createChatVoteResult] 가족 구성원 수: familyCount={}", familyCount);

        // 채팅방에 구독 중인 사용자들을 모두 읽음 처리
        String chatRoomUserKey = "CHAT_ROOM_USER_COUNT_" + roomId;
        Set<String> currentUserIds = redisUtil.getSetMembers(chatRoomUserKey);
        Set<Long> readByUserIds = currentUserIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        log.info("[createChatVoteResult] 읽음 처리 완료 readByUserIds={}", readByUserIds.toArray(new Long[0]));

        // 투표 결과 채팅 객체 생성
        Chat voteResultChat = Chat.builder()
                .roomId(roomId)
                .messageType(MessageType.VOTE_RESULT)
                .senderId(vote.getSenderId())
                .familyCount(familyCount)
                .content(vote.getVoteTitle())
                .createdAt(LocalDateTime.now())
                .voteId(voteId)
                .responseOfVote("")
                .resultOfVote(vote.getVoteResult())
                .readByUserIds(readByUserIds) // 모든 구독 중인 사용자 읽음 처리
                .build();

        String unreadCountKey = "UNREAD_COUNT_" + roomId + "_" + voteResultChat.getChatId();
        redisUtil.setString(unreadCountKey, String.valueOf(familyCount - readByUserIds.size()));

        chatRepository.save(voteResultChat); // 저장 완료
        chatRoomService.notifyReadStatusUpdate(roomId);
        log.info("[createChatVoteResult] 저장된 채팅 정보 voteResultChat={}", voteResultChat);

        return voteResultChat;
    }

    @Override
    public ChatResponse findChat(Chat chat, Long userId) {
        log.info("[findChat] 채팅 정보: chat={}", chat);

        UserInfoResponse user = userServiceFeignClient.getUser(chat.getSenderId()).getData();
        log.info("[findChat] 발신자 정보: userId={}, userNickname={}", user.getUserId(), user.getUserNickname());
        Vote vote = null;

        if (chat.getMessageType().equals(MessageType.VOTE) || chat.getMessageType().equals(MessageType.VOTE_RESPONSE) || chat.getMessageType().equals(MessageType.VOTE_RESULT)) {
            vote = voteRepository.findByVoteId(chat.getVoteId()).orElse(null);
            log.info("[findChat] 투표 정보: vote={}", vote);
        }

        // Redis에서 읽은 사용자 ID 조회
        String readStatusKey = "READ_STATUS_" + chat.getRoomId() + "_" + chat.getChatId();
        Set<String> readUserIds = redisUtil.getSetMembers(readStatusKey);

        // Redis에 사용자 읽음 상태 추가 (본인이 보낸 경우 바로 추가)
        if (chat.getSenderId().equals(userId)) {
            redisUtil.insertSet(readStatusKey, String.valueOf(userId));
            readUserIds.add(String.valueOf(userId)); // 본인이 보낸 메시지는 자동 읽음 처리
        }

        // Redis에서 읽음 상태 기반으로 읽지 않은 사람 수 계산
        int unReadMembers = chat.getFamilyCount() - readUserIds.size();
        unReadMembers = Math.max(unReadMembers, 0); // 0 미만일 경우 0으로 설정

        ChatResponse response = ChatResponse.builder()
                .chatId(chat.getChatId())
                .roomId(chat.getRoomId())
                .messageType(chat.getMessageType())
                .senderId(chat.getSenderId())
                .sender(user)
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .vote(vote)
                .responseOfVote(chat.getResponseOfVote())
                .resultOfVote(chat.getResultOfVote())
                .unReadMembers(unReadMembers) // 읽지 않은 사람 수 설정
                .build();
        log.info("[findChat] 찾은 채팅: chatId={}, unReadMembers={}", chat.getChatId(), unReadMembers);

        return response;
    }


}
