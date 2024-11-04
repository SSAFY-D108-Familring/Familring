package com.familring.calendarservice.controller;

import com.familring.calendarservice.dto.response.DailyDateResponse;
import com.familring.calendarservice.dto.response.MonthDailyScheduleResponse;
import com.familring.calendarservice.dto.response.ScheduleDateResponse;
import com.familring.calendarservice.service.DailyService;
import com.familring.calendarservice.service.ScheduleService;
import com.familring.common_service.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendars")
@RequiredArgsConstructor
@Tag(name = "달력 컨트롤러", description = "일정, 일상 관리")
public class CalendarController {

    private final DailyService dailyService;
    private final ScheduleService scheduleService;

    @GetMapping
    @Operation(summary = "월별 일정 일상 조회", description = "월별 일정과 일상들의 ID와 날짜를 반환")
    public ResponseEntity<BaseResponse<MonthDailyScheduleResponse>> getSchedulesAndDailiesByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        List<DailyDateResponse> dailyDateResponses = dailyService.getDailiesDateByMonth(year, month, userId);
        List<ScheduleDateResponse> scheduleDateResponses = scheduleService.getSchedulesByMonth(year, month, userId);
        MonthDailyScheduleResponse response = MonthDailyScheduleResponse.builder().dailies(dailyDateResponses).schedules(scheduleDateResponses).build();
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), month + "월 일정과 일상 게시물을 모두 조회했습니다.", response));
    }

}
