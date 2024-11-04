package com.familring.calendarservice.service;

import com.familring.calendarservice.domain.Schedule;
import com.familring.calendarservice.domain.ScheduleUser;
import com.familring.calendarservice.dto.client.UserInfoResponse;
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
import java.util.Optional;
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

        // 가족 구성원 정보 조회
        List<UserInfoResponse> familyUsersInfo = familyServiceFeignClient.getFamilyMembers(userId).getData();

        return schedules.stream().map(s -> {
            // 해당 일정으로 생성된 앨범이 있는지 확인
            Boolean hasAlbum = false; // 일단은 미구현이므로 false

            // 스케줄 DTO로 변환
            ScheduleResponse response = ScheduleResponse.builder().id(s.getId()).title(s.getTitle()).startTime(s.getStartTime())
                    .endTime(s.getEndTime()).hasTime(s.getHasTime()).hasNotification(s.getHasNotification()).hasAlbum(hasAlbum)
                    .color(s.getColor()).build();

            // 해당 스케줄에 참여하는 가족 정보 조회
            Map<Long, Boolean> attendanceMap = scheduleUserRepository.findBySchedule(s).stream()
                    .collect(Collectors.toMap(ScheduleUser::getAttendeeId, ScheduleUser::getAttendanceStatus));

            // 해당 스케줄에 참여하는 가족 정보 + 미참여 가족 정보 넣어주기
            familyUsersInfo.forEach(user -> {
                Boolean attendanceStatus = attendanceMap.getOrDefault(user.getUserId(), false);
                response.getUserInfoResponses().add(ScheduleUserResponse.builder().userId(user.getUserId())
                        .userNickname(user.getUserNickname()).userZodiacSign(user.getUserZodiacSign())
                        .userColor(user.getUserColor()).attendanceStatus(attendanceStatus).build()
                );
            });

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
                    schedule.addUser(userAttendance.getUserId(), userAttendance.getAttendanceStatus());
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
    public void updateSchedule(Long scheduleId, ScheduleUpdateRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

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

        request.getAttendances().forEach(userAttendance -> {
            // 해당 스케줄에 참여하는 회원을 조회 후 존재하면 참석 여부 update, 없으면 새로 추가함
            Optional<ScheduleUser> scheduleUser = schedule.getScheduleUsers().stream()
                    .filter(su -> su.getAttendeeId().equals(userAttendance.getUserId()))
                    .findAny();

            if (scheduleUser.isPresent()) {
                scheduleUser.get().updateAttendanceStatus(userAttendance.getAttendanceStatus());
            } else {
                schedule.addUser(userAttendance.getUserId(), userAttendance.getAttendanceStatus());
            }
        });
    }
}
