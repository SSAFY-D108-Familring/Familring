package com.familring.calendarservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUserResponse {
    private Long userId;
    private String userNickname;
    private String userZodiacSign;
    private String userColor;
    private Boolean attendanceStatus;
}
