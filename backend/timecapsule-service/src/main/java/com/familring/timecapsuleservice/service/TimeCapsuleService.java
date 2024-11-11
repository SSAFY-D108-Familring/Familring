package com.familring.timecapsuleservice.service;

import com.familring.timecapsuleservice.domain.TimeCapsule;
import com.familring.timecapsuleservice.domain.TimeCapsuleAnswer;
import com.familring.timecapsuleservice.dto.client.Family;
import com.familring.timecapsuleservice.dto.client.FamilyStatusRequest;
import com.familring.timecapsuleservice.dto.client.UserInfoResponse;
import com.familring.timecapsuleservice.dto.request.TimeCapsuleAnswerCreateRequest;
import com.familring.timecapsuleservice.dto.request.TimeCapsuleCreateRequest;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleAnswerItem;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleItem;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleListResponse;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleStatusResponse;
import com.familring.timecapsuleservice.exception.AlreadyExistTimeCapsuleAnswerException;
import com.familring.timecapsuleservice.exception.AlreadyExistTimeCapsuleException;
import com.familring.timecapsuleservice.exception.ExpiredTimeCapsuleAnswerException;
import com.familring.timecapsuleservice.exception.TimeCapsuleNotFoundException;
import com.familring.timecapsuleservice.exception.client.FamilyNotFoundException;
import com.familring.timecapsuleservice.repository.TimeCapsuleAnswerRepository;
import com.familring.timecapsuleservice.repository.TimeCapsuleRepository;
import com.familring.timecapsuleservice.service.client.FamilyServiceFeignClient;
import com.familring.timecapsuleservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@RequiredArgsConstructor
@Transactional
public class TimeCapsuleService {

    private final TimeCapsuleRepository timeCapsuleRepository;
    private final TimeCapsuleAnswerRepository timeCapsuleAnswerRepository;
    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final UserServiceFeignClient userServiceFeignClient;

    // 상태 관리 (3가지 상태로 구분)
    public TimeCapsuleStatusResponse getTimeCapsuleStatus(Long userId) {
        TimeCapsuleStatusResponse response = null;

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();

        if (family == null) {
            throw new FamilyNotFoundException();
        }

        Long familyId = family.getFamilyId();

        // 1. 작성할 수 있는 타임캡슐이 아예 없는 경우 (0)
        // 현재 날짜를 기준으로 해당 날짜가 포함된 타임캡슐이 없으면 작성할 수 있는 타임캡슐이 없는 경우
        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        Optional<TimeCapsule> timeCapsuleOpt  = timeCapsuleRepository.findTimeCapsuleWithinDateRangeAndFamilyId(currentDate, familyId);
        int cnt = timeCapsuleRepository.countByFamilyId(familyId);

        if (timeCapsuleOpt.isEmpty()) {
            response = TimeCapsuleStatusResponse.builder()
                    .status(0) // 상태값만 전송
                    .build();
        } else {
            // 현재 날짜 기준으로 답변할 수 있는 타임캡슐이 있을 때
            TimeCapsule timeCapsule = timeCapsuleOpt.get();

            // 2. 이미 작성을 끝낸 상태 (1) - 해당 user 가 이미 작성을 한 경우
            // 타임 캡슐 답변 DB 에서 해당 User 로 작성된 타임 캡슐이 있으면
            Optional<TimeCapsuleAnswer> timeCapsuleAnswer = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByUserIdAndTimecapsule(userId, timeCapsule);
            if (timeCapsuleAnswer.isPresent()) {
                // 타임 캡슐 마감 날짜 - 현재 날짜 = 남은 날짜 같이 전송
                int dayCount = (int) ChronoUnit.DAYS.between(currentDate, timeCapsule.getEndDate());

                if (dayCount > 0) {
                    response = TimeCapsuleStatusResponse.builder()
                            .status(1)
                            .dayCount(dayCount) // 남은 날짜 전송
                            .build();
                } else { // dayCount == 0 (0 일이면 타임 캡슐 생성할 수 있게)
                    response = TimeCapsuleStatusResponse.builder()
                            .status(0) // 상태값만 전송
                            .build();
                }
            } else {
                // 3. 지금 작성 가능한 상태 (2) - 해당 user 가 작성 안 한 경우
                // 작성한 가족 구성원 목록 조회
                List<UserInfoResponse> userInfoResponses = familyServiceFeignClient.getFamilyMemberList(userId).getData();
                List<Long> userIds = new ArrayList<>();
                for(UserInfoResponse userInfoResponse : userInfoResponses) {
                    // timeCapsule answer DB 에 있는 구성원 id 들 찾아서
                    Optional<TimeCapsuleAnswer> answer = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByUserIdAndTimecapsule(userInfoResponse.getUserId(), timeCapsule);

                    // userInfoResponse.getUserId() 가 timecapsule 에 있으면
                    // DB에 해당 userId로 작성된 답변이 있는 경우 userIds에 추가
                    // uesrIds 에 userInfoResponse.getUserId() 를 추가
                    answer.ifPresent(capsuleAnswer -> userIds.add(capsuleAnswer.getUserId()));
                }
                // 그 찾은 user id 들로 userResponse 조회
                List<UserInfoResponse> users = userServiceFeignClient.getAllUser(userIds).getData();

                response = TimeCapsuleStatusResponse.builder()
                        .status(2)
                        .count(cnt) // 몇 번째 타임캡슐인지
                        .users(users)
                        .build();
            }
        }

        return response;
    }

    // 타임캡슐 생성
    public void createTimeCapsule(Long userId, TimeCapsuleCreateRequest timeCapsuleCreateRequest) {
        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 현재 날짜로 조회했을 때 타임캡슐이 없을 때만 생성 가능
        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        Optional<TimeCapsule> timeCapsuleOpt = timeCapsuleRepository.findTimeCapsuleWithinDateRangeAndFamilyId(currentDate, familyId);

        TimeCapsule timeCapsule;
        if (timeCapsuleOpt.isEmpty()) {
            timeCapsule = TimeCapsule.builder()
                    .familyId(familyId)
                    .startDate(LocalDate.now())
                    .endDate(timeCapsuleCreateRequest.getDate())
                    .build();
        } else { // 만약에 타임캡슐이 이미 있을 경우 throw
            throw new AlreadyExistTimeCapsuleException();
        }

        timeCapsuleRepository.save(timeCapsule);
    }

    // 타임캡슐 답변 생성 (타임캡슐 생성 일자 부터 최소 1일 까지만 작성 가능)
    public void createTimeCapsuleAnswer(Long userId, TimeCapsuleAnswerCreateRequest timeCapsuleAnswerCreateRequest) {
        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 현재 날짜로 조회했을 때 타임캡슐이 없을 때만 생성 가능
        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        Optional<TimeCapsule> timeCapsuleOpt = timeCapsuleRepository.findTimeCapsuleWithinDateRangeAndFamilyId(currentDate, familyId);

        TimeCapsuleAnswer timeCapsuleAnswer = null;
        // 타임 캡슐이 있을 경우
        if (timeCapsuleOpt.isPresent()) {
            int dayDiff = (int) ChronoUnit.DAYS.between(currentDate, timeCapsuleOpt.get().getEndDate());
            if (dayDiff>=1) { // 타임 캡슐 생성 일자, 현재 날짜 차이가 1일 보다 크기만 하면
                Optional<TimeCapsuleAnswer> answer = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByUserIdAndTimecapsule(userId, timeCapsuleOpt.get());
                if(answer.isEmpty()) {
                    // 작성한 답변이 없을 경우
                    timeCapsuleAnswer = TimeCapsuleAnswer.builder()
                            .timecapsule(timeCapsuleOpt.get())
                            .content(timeCapsuleAnswerCreateRequest.getContent())
                            .createAt(currentDate)
                            .userId(userId)
                            .build();
                } else {
                    throw new AlreadyExistTimeCapsuleAnswerException(); // 이미 답변 작성함
                }
            } else {
                throw new ExpiredTimeCapsuleAnswerException();
            }
        } else {
            throw new TimeCapsuleNotFoundException(); // 타임 캡슐이 없으면 답변 작성 불가
        }

        timeCapsuleAnswerRepository.save(timeCapsuleAnswer);

        FamilyStatusRequest familyStatusRequest = FamilyStatusRequest
                .builder()
                .familyId(familyId)
                .amount(3)
                .build();
        familyServiceFeignClient.updateFamilyStatus(familyStatusRequest);

    }

    // 타임 캡슐 목록 조회
    public TimeCapsuleListResponse getTimeCapsuleList(Long userId, int pageNo) {

        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        PageRequest pageRequest = PageRequest.of(pageNo, 18); // 18개씩
        Slice<TimeCapsule> timeCapsuleSlice = timeCapsuleRepository.findTimeCapsulesByFamilyIdOrderByStartDateDesc(familyId, pageRequest);
        List<TimeCapsule> timeCapsuleList = timeCapsuleSlice.getContent();

        // 전체 타임캡슐 개수 조회
        int totalCount = timeCapsuleRepository.countByFamilyId(familyId);

        List<TimeCapsuleItem> timeCapsuleItems = IntStream.range(0, timeCapsuleList.size())
                .mapToObj(index -> {
                    TimeCapsule timeCapsule = timeCapsuleList.get(index);

                    // 특정 타임캡슐의 모든 답변 가져오기
                    List<TimeCapsuleAnswer> answers = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByTimecapsule(timeCapsule);

                    // TimeCapsuleAnswerItem 리스트로 변환
                    List<TimeCapsuleAnswerItem> answerItems = answers.stream()
                            .map(answer -> {
                                // userId로 사용자 정보 가져오기
                                Long id = timeCapsuleAnswerRepository.findUserIdByIdAndTimecapsule(answer.getId(), timeCapsule);
                                UserInfoResponse user = userServiceFeignClient.getAllUser(Collections.singletonList(id)).getData().get(0);

                                return TimeCapsuleAnswerItem.builder()
                                        .userNickname(user.getUserNickname())
                                        .userZodiacSign(user.getUserZodiacSign())
                                        .userColor(user.getUserColor())
                                        .content(answer.getContent())
                                        .date(answer.getCreateAt())
                                        .build();
                            }).collect(Collectors.toList());

                    // TimeCapsuleItem 생성
                    return TimeCapsuleItem.builder()
                            .timeCapsuleId(timeCapsule.getId())
                            .date(timeCapsule.getEndDate()) // LocalDate로 변환
                            .index(totalCount - (pageNo * 18 + index)) // 현재 페이지와 index 를 이용해 순서 계산
                            .items(answerItems)
                            .build();
                }).collect(Collectors.toList());

        // TimeCapsuleListResponse (Slice 적용)
        return TimeCapsuleListResponse.builder()
                .hasNext(timeCapsuleSlice.hasNext())
                .isLast(timeCapsuleSlice.isLast())
                .pageNo(pageNo)
                .items(timeCapsuleItems)
                .build();
    }
}

