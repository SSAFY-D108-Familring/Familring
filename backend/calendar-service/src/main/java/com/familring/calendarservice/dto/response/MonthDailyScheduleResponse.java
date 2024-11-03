package com.familring.calendarservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MonthDailyScheduleResponse {
    private List<DailyDateResponse> dailies;
    private List<ScheduleDateResponse> schedules;
}
