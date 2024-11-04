package com.familring.calendarservice.service;

import com.familring.calendarservice.domain.Schedule;
import com.familring.calendarservice.dto.response.ScheduleDateResponse;
import com.familring.calendarservice.dto.response.ScheduleResponse;
import com.familring.calendarservice.dto.response.ScheduleUserResponse;
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
        return scheduleRepository.findSchedulesByMonthAndFamilyId(year, month, familyId).stream().map(
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
            // 스케줄 DTO로 변환
            ScheduleResponse response = ScheduleResponse.builder().id(s.getId()).title(s.getTitle()).startTime(s.getStartTime())
                    .endTime(s.getEndTime()).hasTime(s.getHasTime()).hasNotification(s.getHasNotification())
                    .color(s.getColor()).build();
            // 해당 스케줄에 참여하는 회원 바인딩
            scheduleUserRepository.findBySchedule(s).forEach(su -> response.getUserInfoResponses().add(userMap.get(su.getUserId())));
            return response;
        }).toList();
    }
//
//    public void createSchedule() {
//        scheduleRepository.
//    }


}
