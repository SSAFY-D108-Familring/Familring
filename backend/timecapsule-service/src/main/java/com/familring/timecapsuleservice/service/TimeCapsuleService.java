package com.familring.timecapsuleservice.service;

import com.familring.common_service.dto.BaseResponse;
import com.familring.timecapsuleservice.domain.TimeCapsule;
import com.familring.timecapsuleservice.domain.TimeCapsuleAnswer;
import com.familring.timecapsuleservice.dto.client.FamilyDto;
import com.familring.timecapsuleservice.dto.client.UserInfoResponse;
import com.familring.timecapsuleservice.dto.request.TimeCapsuleCreateRequest;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleStatusResponse;
import com.familring.timecapsuleservice.exception.FamilyNotFoundException;
import com.familring.timecapsuleservice.repository.TimeCapsuleAnswerRepository;
import com.familring.timecapsuleservice.repository.TimeCapsuleRepository;
import com.familring.timecapsuleservice.service.client.FamilyServiceFeignClient;
import com.familring.timecapsuleservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        FamilyDto familyDto = familyServiceFeignClient.getFamilyInfo(userId).getData();

        if (familyDto == null) {
            throw new FamilyNotFoundException();
        }

        Long familyId = familyDto.getFamilyId();

        // 1. 작성할 수 있는 타임캡슐이 아예 없는 경우 (0)
        // 현재 날짜를 기준으로 해당 날짜가 포함된 타임캡슐이 없으면 작성할 수 있는 타임캡슐이 없는 경우
        LocalDateTime currentDate = LocalDateTime.now(); // 현재 날짜
        Optional<TimeCapsule> timeCapsuleOpt  = timeCapsuleRepository.findTimeCapsuleWithinDateRangeAndFamilyId(currentDate, familyId);

        if(timeCapsuleOpt .isEmpty()) {
            response = TimeCapsuleStatusResponse.builder()
                    .status(0) // 상태값만 전송
                    .build();
        } else {
            // 현재 날짜 기준으로 답변할 수 있는 타임캡슐이 있을 때
            TimeCapsule timeCapsule = timeCapsuleOpt.get();

            // 2. 이미 작성을 끝낸 상태 (1) - 해당 user 가 이미 작성을 한 경우
            // 타임 캡슐 답변 DB 에서 해당 User 로 작성된 타임 캡슐이 있으면
            Optional<TimeCapsuleAnswer> timeCapsuleAnswer = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByUserIdAndTimecapsule(userId, timeCapsule);
            if(timeCapsuleAnswer.isPresent()) {
                // 타임 캡슐 마감 날짜 - 현재 날짜 = 남은 날짜 같이 전송
                int dayCount = (int) ChronoUnit.DAYS.between(currentDate, timeCapsule.getEndDate());

                response = TimeCapsuleStatusResponse.builder()
                        .status(1)
                        .dayCount(dayCount) // 남은 날짜 전송
                        .build();

            } else {
                // 3. 지금 작성 가능한 상태 (2) - 해당 user 가 작성 안 한 경우
                // 작성한 가족 구성원 목록 조회
                List<UserInfoResponse> userInfoResponses = familyServiceFeignClient.getFamilyMemberList(userId).getData();
                List<Long> userIds = new ArrayList<>();
                for(UserInfoResponse userInfoResponse : userInfoResponses) {
                    // timeCapsule answer DB 에 있는 구성원 id 들 찾아서
                    Optional<TimeCapsuleAnswer> answer = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByUserIdAndTimecapsule(userInfoResponse.getUserId(), timeCapsule);

                    if (answer.isPresent()) { // userInfoResponse.getUserId() 가 timecapsule 에 있으면
                        // DB에 해당 userId로 작성된 답변이 있는 경우 userIds에 추가
                        // uesrIds 에 userInfoResponse.getUserId() 를 추가
                        userIds.add(userId);
                    }
                }
                // 그 찾은 user id 들로 userResponse 조회
                // 이 구성원 목록 for 문으로 돌면서
                List<UserInfoResponse> users = userServiceFeignClient.getAllUser(userIds).getData();

                response = TimeCapsuleStatusResponse.builder()
                        .status(2)
                        .count(timeCapsule.getId()) // 몇 번째 타임캡슐인지
                        .users(users)
                        .build();
            }
        }

        return response;
    }

    // 타임캡슐 생성
    public void createTimeCapsule(Long userId, TimeCapsuleCreateRequest timeCapsuleCreateRequest) {
        // 가족 조회
        FamilyDto familyDto = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = familyDto.getFamilyId();

        TimeCapsule timeCapsule = TimeCapsule.builder()
                .familyId(familyId)
                .startDate(LocalDateTime.now())
                .endDate(timeCapsuleCreateRequest.getDate())
                .build();

        timeCapsuleRepository.save(timeCapsule);
    }

}
