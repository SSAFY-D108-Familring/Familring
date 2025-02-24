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
import com.familring.timecapsuleservice.exception.*;
import com.familring.timecapsuleservice.exception.client.FamilyNotFoundException;
import com.familring.timecapsuleservice.repository.TimeCapsuleAnswerRepository;
import com.familring.timecapsuleservice.repository.TimeCapsuleRepository;
import com.familring.timecapsuleservice.service.client.FamilyServiceFeignClient;
import com.familring.timecapsuleservice.service.client.UserServiceFeignClient;
import com.familring.timecapsuleservice.service.job.TimeCapsuleNotificationJob;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TimeCapsuleService {

    private final TimeCapsuleRepository timeCapsuleRepository;
    private final TimeCapsuleAnswerRepository timeCapsuleAnswerRepository;
    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final UserServiceFeignClient userServiceFeignClient;
    private final Scheduler notificationScheduler;

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
        // 그 가족의 가장 최근의 타임캡슐을 찾아와서
        Optional<TimeCapsule> timeCapsuleOpt = timeCapsuleRepository.findFirstByFamilyIdOrderByIdDesc(familyId);


        // 현재 날짜로 조회
        LocalDate currentDate = LocalDate.now(); // 현재 날짜
        int cnt = timeCapsuleRepository.countByFamilyId(familyId);

        // 최근 타임캡슐이 없으면 무조건 타임 캡슐 생성 가능 status 0 이고
        if (timeCapsuleOpt.isEmpty()) {
            response = TimeCapsuleStatusResponse.builder()
                    .status(0) // 상태값만 전송
                    .build();
        } else {
            TimeCapsule timeCapsule = timeCapsuleOpt.get();
            int dayCount = (int) ChronoUnit.DAYS.between(currentDate, timeCapsule.getEndDate());
            // 최근 타임 캡슐이 끝나는 날짜가 오늘 날짜면 타임 캡슐 생성 가능
            if (dayCount == 0) { // 최근 타임 캡슐이 끝나는 날짜가 오늘 날짜면 생성 가능
                response = TimeCapsuleStatusResponse.builder()
                        .status(0)
                        .dayCount(dayCount) // 남은 날짜 전송
                        .build();
            } else if (dayCount > 0) {
                // 타임 캡슐이 끝나는 날짜가 오늘 날짜보다 이후면 답변 작성 가능
                // 2. 이미 작성을 끝낸 상태 (1) - 해당 user 가 이미 작성을 한 경우
                Optional<TimeCapsuleAnswer> timeCapsuleAnswer = timeCapsuleAnswerRepository.getTimeCapsuleAnswerByUserIdAndTimecapsule(userId, timeCapsule);

                if (timeCapsuleAnswer.isPresent()) {
                    response = TimeCapsuleStatusResponse.builder()
                            .status(1)
                            .dayCount(dayCount) // 남은 날짜 전송
                            .build();
                } else {
                    // 3. 지금 작성 가능한 상태 (2) - 해당 user 가 작성 안 한 경우
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
        }

        return response;
    }

    // 타임캡슐 생성
    public void createTimeCapsule(Long userId, TimeCapsuleCreateRequest timeCapsuleCreateRequest) {
        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 그 가족의 가장 최근의 타임캡슐을 찾아와서
        Optional<TimeCapsule> timeCapsuleOpt = timeCapsuleRepository.findFirstByFamilyIdOrderByIdDesc(familyId);

        TimeCapsule timeCapsule;
        // 현재 날짜로 조회
        LocalDate currentDate = LocalDate.now(); // 현재 날짜

        String uniqueId = UUID.randomUUID().toString();

        // Job 설정
        JobDetail jobDetail = JobBuilder.newJob(TimeCapsuleNotificationJob.class)
                .withIdentity("timeCapsuleNotificationJob_" + uniqueId)
                .usingJobData("userId", userId)
                .build();

        // LocalDate를 Date로 변환
        Date triggerStartDate = Date.from(timeCapsuleCreateRequest.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        // 지정된 endDate에 Job이 실행되도록 트리거 생성
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("timeCapsuleNotificationTrigger_" + uniqueId)
                .startAt(triggerStartDate)
                .build();

        timeCapsule = TimeCapsule.builder()
                .familyId(familyId)
                .startDate(LocalDate.now())
                .endDate(timeCapsuleCreateRequest.getDate())
                .build();

        // 없으면 생성 가능 (무조건)
        if (timeCapsuleOpt.isEmpty()) {
            try {
                notificationScheduler.scheduleJob(jobDetail, trigger);
                log.info("스케쥴러 성공");
            } catch (Exception e) {
                log.info(e.toString());
                throw new FailedCreateTimeCapsuleException();
            }

            timeCapsuleRepository.save(timeCapsule);
        } else {
            // 만약 타임캡슐이 있으면 그 타임캡슐의 endDate 가 오늘 날짜 or endDate (11.15) 보다 이후면 (11.16) 생성 가능
            int dayDiff = (int) ChronoUnit.DAYS.between(currentDate, timeCapsule.getEndDate());
            if (dayDiff >= 0) {
                try {
                    notificationScheduler.scheduleJob(jobDetail, trigger);
                    log.info("스케쥴러 성공");
                } catch (Exception e) {
                    log.info(e.toString());
                    throw new FailedCreateTimeCapsuleException();
                }

                timeCapsuleRepository.save(timeCapsule);
            }
        }

    }

    // 타임캡슐 답변 생성
    public void createTimeCapsuleAnswer(Long userId, TimeCapsuleAnswerCreateRequest timeCapsuleAnswerCreateRequest) {
        // 가족 조회
        Family family = familyServiceFeignClient.getFamilyInfo(userId).getData();
        Long familyId = family.getFamilyId();

        // 가장 최근 타임 캡슐을 조회했을 때 있을 때만 생성 가능
        // 그 가족의 가장 최근의 타임캡슐을 찾아와서
        Optional<TimeCapsule> timeCapsuleOpt = timeCapsuleRepository.findFirstByFamilyIdOrderByIdDesc(familyId);

        // 현재 날짜로 조회
        LocalDate currentDate = LocalDate.now(); // 현재 날짜

        TimeCapsuleAnswer timeCapsuleAnswer;
        // 타임 캡슐이 있을 경우
        if (timeCapsuleOpt.isPresent()) {
            // 가장 최근 타임 캡슐의 endDate 날짜가 11월 15일이면 11월 14일까지 작성 가능 (endDate 날짜보다 전날까지만 작성 가능)
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

