package com.familring.calendarservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAttendance {
    private Long userId;
    private Boolean attendanceStatus;
}
