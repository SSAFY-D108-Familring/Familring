package com.familring.familyservice.service.chat;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.exception.chat.AlreadyParticipatedException;
import com.familring.familyservice.exception.chat.VoteNotFoundException;
import com.familring.familyservice.model.dao.ChatRepository;
import com.familring.familyservice.model.dao.FamilyDao;
import com.familring.familyservice.model.dao.VoteRepository;
import com.familring.familyservice.model.dto.Chat;
import com.familring.familyservice.model.dto.Vote;
import com.familring.familyservice.model.dto.request.ChatSendRequest;
import com.familring.familyservice.model.dto.request.VoteParticipationRequest;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private static final int DEFAULT_PAGE_SIZE = 50; // 기본 페이지 사이즈 설정

    private final FamilyDao familyDao;

    private final ChatRepository chatRepository;
    private final VoteRepository voteRepository;

    private final UserServiceFeignClient userServiceFeignClient;

    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public Page<Chat> getMessagesByFamilyId(String familyId, int page) {
        // 내림차순으로 정렬하여 최신 메시지가 먼저 나오게 설정
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));

        return chatRepository.findByFamilyId(familyId, pageable);
    }

    @Override
    public Mono<List<UserInfoResponse>> getVoteParticipants(String voteId) {
        return Mono.create(sink -> {
            // 별도의 스레드에서 실행
            new Thread(() -> {
                try {
                    // 1. voteId로 투표 조회
                    Vote vote = voteRepository.findById(voteId)
                            .switchIfEmpty(Mono.error(new RuntimeException("투표를 찾을 수 없습니다.")))
                            .block();

                    // 2. participantChoices의 keySet으로 참여자 ID 목록을 Long 타입으로 변환
                    List<Long> userIdList = vote.getParticipantsChoices().keySet().stream()
                            .map(Long::parseLong)
                            .collect(Collectors.toList());

                    // 3. FeignClient를 동기 호출하여 사용자 정보 가져오기
                    BaseResponse<List<UserInfoResponse>> userInfoResponse = userServiceFeignClient.getAllUser(userIdList);

                    // 4. FeignClient의 응답에서 List<UserInfoResponse>를 Mono로 반환
                    sink.success(userInfoResponse.getData());
                } catch (Exception e) {
                    sink.error(e); // 예외 발생 시 sink에 에러 전달
                }
            }).start();
        });
    }

    @Override
    @Transactional
    public Mono<Chat> sendMessage(ChatSendRequest chatSendRequest) {
        // ChatSendRequest를 Chat 객체로 변환
        Chat chat = Chat.builder()
                .familyId(chatSendRequest.getFamilyId())
                .userId(chatSendRequest.getUserId())
                .comment(chatSendRequest.getComment())
                .createdAt(LocalDateTime.now())
                .isVote(chatSendRequest.isVote())
                .build();

        // 투표 메시지일 경우 Vote 객체 생성
        if (chatSendRequest.isVote()) {
            Vote vote = Vote.builder()
                    .voteTitle(chatSendRequest.getVoteTitle())
                    .createdAt(LocalDateTime.now())
                    .participantsChoices(new HashMap<>()) // 사용자 선택 초기화
                    .isCompleted(false) // 투표 완료 여부 초기화
                    .voteResult(new HashMap<>()) // 결과 초기화
                    .build();

            // 생성된 Vote 객체를 저장하고 voteId를 Chat 객체에 설정
            vote = voteRepository.save(vote).block(); // Mono에서 동기식으로 값을 추출

            if (vote != null) {
                chat.setVoteId(vote.getId()); // 생성된 Vote의 ID를 Chat에 설정
            }
        }

        // MongoDB에 저장하고 저장된 Chat 객체 반환
        return chatRepository.save(chat);
    }

    @Override
    @Transactional
    public void participateInVote(VoteParticipationRequest voteRequest) {
        Vote vote = voteRepository.findById(voteRequest.getVoteId())
                .switchIfEmpty(Mono.error(new VoteNotFoundException()))
                .block();

        try {
            // 2. 이미 참여한 경우 예외 발생
            vote.addParticipant(voteRequest.getUserId().toString(), voteRequest.getVoteComment());

            // 3-1. 가족 구성원 모두 참여했는지 확인
            int familyCount = familyDao.countFamilyUserByUserId(voteRequest.getUserId());
            if(familyCount == vote.getParticipantsChoices().size()) {
                // 3-2. 투표 종료하기 -> isCompleted true로 변경
                vote.setCompleted(true);

                // 3-3. 모두 참여한 경우, 투표 결과를 채팅창에 보이도록 설정
                messagingTemplate.convertAndSend("/topic/chat/" + voteRequest.getFamilyId(), vote.getVoteResult());
                log.info("Vote result sent: {}", vote.getVoteResult());
            }

            // 투표 저장
            voteRepository.save(vote);
        } catch (AlreadyParticipatedException e) {
            // 이미 참여한 경우 예외 처리
            throw new AlreadyParticipatedException();
        }
    }
}
