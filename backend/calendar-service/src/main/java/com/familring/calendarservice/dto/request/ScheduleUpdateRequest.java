package com.familring.calendarservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ScheduleUpdateRequest {
    private Long id;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean hasTime;

    private Boolean hasNotification;

    private String color;

    private List<UserAttendance> attendances;
}

