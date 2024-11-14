package com.familring.calendarservice.service;

import com.familring.calendarservice.domain.Schedule;
import com.familring.calendarservice.domain.ScheduleUser;
import com.familring.calendarservice.dto.client.NotificationRequest;
import com.familring.calendarservice.dto.client.NotificationType;
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
import com.familring.calendarservice.service.client.AlbumServiceFeignClient;
import com.familring.calendarservice.service.client.FamilyServiceFeignClient;
import com.familring.calendarservice.repository.ScheduleRepository;
import com.familring.calendarservice.service.client.NotificationServiceFeignClient;
import com.familring.calendarservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.familring.calendarservice.dto.client.NotificationType.MENTION_SCHEDULE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final FamilyServiceFeignClient familyServiceFeignClient;
    private final AlbumServiceFeignClient albumServiceFeignClient;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleUserRepository scheduleUserRepository;
    private final NotificationServiceFeignClient notificationServiceFeignClient;
    private final UserServiceFeignClient userServiceFeignClient;

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
            Long albumId = albumServiceFeignClient.getAlbumIdByScheduleId(s.getId()).getData(); // 일단은 미구현이므로 false

            // 스케줄 DTO로 변환
            ScheduleResponse response = ScheduleResponse.builder().id(s.getId()).title(s.getTitle()).startTime(s.getStartTime())
                    .endTime(s.getEndTime()).albumId(albumId).hasNotification(s.getHasNotification()).hasTime(s.getHasTime())
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

        scheduleRepository.save(schedule);

        // 알림 전송
        if (schedule.getHasNotification()) {
            List<Long> attendeeIds = schedule.getScheduleUsers().stream()
                    .filter(ScheduleUser::getAttendanceStatus)
                    .map(ScheduleUser::getAttendeeId)
                    .filter(id -> !id.equals(userId)).toList();

            UserInfoResponse userInfo = userServiceFeignClient.getUser(userId).getData();
            String title = userInfo.getUserNickname() + "님이 새로운 일정을 등록했어요 \uD83D\uDCC5";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd]");
            String formattedDate = schedule.getStartTime().format(formatter);
            String message = formattedDate + " " + schedule.getTitle();

            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .notificationType(MENTION_SCHEDULE)
                    .receiverUserIds(attendeeIds)
                    .senderUserId(userId)
                    .destinationId(schedule.getId().toString())
                    .title(title)
                    .message(message).build();

            notificationServiceFeignClient.alarmByFcm(notificationRequest);
        }
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        if (!schedule.getFamilyId().equals(familyId)) {
            throw new InvalidScheduleRequestException();
        }

        // 알림 전송
        List<Long> attendeeIds = schedule.getScheduleUsers().stream().map(ScheduleUser::getAttendeeId)
                .filter(id -> !id.equals(userId)).toList();

        UserInfoResponse userInfo = userServiceFeignClient.getUser(userId).getData();
        String title = userInfo.getUserNickname() + "님이 일정을 삭제했어요 \uD83D\uDCC5";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd]");
        String formattedDate = schedule.getStartTime().format(formatter);
        String message = formattedDate + " " + schedule.getTitle();

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .notificationType(MENTION_SCHEDULE)
                .receiverUserIds(attendeeIds)
                .senderUserId(userId)
                .destinationId(schedule.getId().toString())
                .title(title)
                .message(message).build();

        notificationServiceFeignClient.alarmByFcm(notificationRequest);

        scheduleRepository.delete(schedule);
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleUpdateRequest request, Long userId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(ScheduleNotFoundException::new);

        Long familyId = familyServiceFeignClient.getFamilyInfo(userId).getData().getFamilyId();
        if (!schedule.getFamilyId().equals(familyId)) {
            throw new InvalidScheduleRequestException();
        }

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

        // 알림 전송
        if (schedule.getHasNotification()) {
            Stream<Long> updatedAttendeeIds = schedule.getScheduleUsers().stream().filter(ScheduleUser::getAttendanceStatus)
                    .map(ScheduleUser::getAttendeeId);
            Stream<Long> requestAttendeeIds = request.getAttendances().stream().filter(UserAttendance::getAttendanceStatus)
                    .map(UserAttendance::getUserId);

            List<Long> totalAttendeeIds = Stream.concat(updatedAttendeeIds, requestAttendeeIds).distinct()
                    .filter(id -> !id.equals(userId)).toList();

            UserInfoResponse userInfo = userServiceFeignClient.getUser(userId).getData();
            String title = userInfo.getUserNickname() + "님이 일정을 수정했어요 \uD83D\uDCC5";

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy/MM/dd]");
            String formattedDate = schedule.getStartTime().format(formatter);
            String message = formattedDate + " " + schedule.getTitle();

            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .notificationType(MENTION_SCHEDULE)
                    .receiverUserIds(totalAttendeeIds)
                    .senderUserId(userId)
                    .destinationId(schedule.getId().toString())
                    .title(title)
                    .message(message).build();

            notificationServiceFeignClient.alarmByFcm(notificationRequest);
        }
    }
}
