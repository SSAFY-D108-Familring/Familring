package com.familring.calendarservice.dto.response;

import lombok.*;

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

    @Builder.Default
    private List<ScheduleUserResponse> userInfoResponses = new ArrayList<>();

}