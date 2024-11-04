package com.familring.calendarservice.service;

import com.familring.calendarservice.domain.Schedule;
import com.familring.calendarservice.dto.request.ScheduleUpdateRequest;
import com.familring.calendarservice.dto.request.UserAttendance;
import com.familring.calendarservice.dto.response.ScheduleDateResponse;
import com.familring.calendarservice.dto.request.ScheduleRequest;
import com.familring.calendarservice.dto.response.ScheduleResponse;
import com.familring.calendarservice.dto.response.ScheduleUserResponse;
import com.familring.calendarservice.exception.schedule.InvalidScheduleRequestException;
import com.familring.calendarservice.exception.schedule.ScheduleNotFoundException;
import com.familring.calendarservice.repository.ScheduleUserRepository;
import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleUserRepository scheduleUserRepository;

    public List<ScheduleDateResponse> getSchedulesByMonth(int year, int month, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        return scheduleRepository.findSchedulesByDateAndFamilyId(year, month, familyId).stream().map(
                schedule -> ScheduleDateResponse.builder().id(schedule.getId()).title(schedule.getTitle()).startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime()).color(schedule.getColor()).build()).toList();
    }

    public List<ScheduleResponse> getSchedules(List<Long> scheduleIds, Long userId) {
        // 전체 스케줄 조회
        List<Schedule> schedules = scheduleRepository.findAllById(scheduleIds);

        // 가족 정보를 (id, 회원 정보) 형태의 맵으로 변환
        Map<Long, ScheduleUserResponse> userMap = familyServiceFeignClient.getFamilyMembers(userId).getData().stream()
                .map(u -> ScheduleUserResponse.builder()
                        .userId(u.getUserId()).userNickname(u.getUserNickname()).userZodiacSign(u.getUserZodiacSign())
                        .userColor(u.getUserColor()).build()).collect(Collectors.toMap(ScheduleUserResponse::getUserId, u -> u));

        return schedules.stream().map(s -> {
            // 해당 일정으로 생성된 앨범이 있는지 확인
            Boolean hasAlbum = false; // 일단은 미구현이므로 false

            // 스케줄 DTO로 변환
            ScheduleResponse response = ScheduleResponse.builder().id(s.getId()).title(s.getTitle()).startTime(s.getStartTime())
                    .endTime(s.getEndTime()).hasTime(s.getHasTime()).hasNotification(s.getHasNotification()).hasAlbum(false)
                    .color(s.getColor()).build();
            // 해당 스케줄에 참여하는 회원 바인딩
            scheduleUserRepository.findBySchedule(s).forEach(su -> response.getUserInfoResponses().add(userMap.get(su.getUserId())));
            return response;
        }).toList();
    }

    @Transactional
    public void createSchedule(ScheduleRequest request, Long userId) {
        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();

        Schedule schedule = Schedule.builder()
                .familyId(familyId)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .title(request.getTitle())
                .hasNotification(request.getHasNotification())
                .hasTime(request.getHasTime())
                .color(request.getColor()).build();

        request.getAttendances().forEach(
                userAttendance -> {
                    schedule.addUser(userAttendance.getUserId(), userAttendance.getAttendance());
                });

        // @@ 알림 등록해주기 @@

        scheduleRepository.save(schedule);
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        if (!schedule.getFamilyId().equals(familyId)) {
            throw new InvalidScheduleRequestException();
        }

        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void updateSchedule(ScheduleUpdateRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(request.getId()).orElseThrow(ScheduleNotFoundException::new);

        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        if (!schedule.getFamilyId().equals(familyId)) {
            throw new InvalidScheduleRequestException();
        }

        /* 알람 수정해주기
        ON -> OFF 알람 삭제
        OFF -> ON 알람 등록
        ON -> ON 알람 시간 변경해야 하는지 체크
         */

        schedule.updateSchedule(
                request.getStartTime(),
                request.getEndTime(),
                request.getTitle(),
                request.getHasNotification(),
                request.getHasTime(),
                request.getColor()
        );

        Map<Long, Boolean> attendanceMap = request.getAttendances().stream().collect(Collectors.toMap(
                UserAttendance::getUserId,
                UserAttendance::getAttendance
        ));

        schedule.getScheduleUsers().forEach(scheduleUser -> {
            scheduleUser.updateAttendanceStatus(attendanceMap.get(scheduleUser.getUserId()));
        });

    }
}
