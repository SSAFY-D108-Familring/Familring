package com.familring.calendarservice.dto.response;

import com.familring.calendarservice.domain.ScheduleUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {

    private Long id;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean hasTime;

    private Boolean hasNotification;

    private String color;

}

    private Long id;

    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean hasTime;

    private Boolean hasNotification;

    private String color;

    함께하는 구성원 정보

