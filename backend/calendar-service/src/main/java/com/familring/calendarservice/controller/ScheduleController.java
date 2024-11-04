package com.familring.calendarservice.controller;

import com.familring.calendarservice.dto.response.ScheduleRequest;
import com.familring.calendarservice.dto.response.ScheduleResponse;
import com.familring.calendarservice.service.ScheduleService;
import com.familring.common_service.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendars/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping()
    @Operation(summary = "다중 일정 조회", description = "일정들의 세부 정보를 조회합니다.")
    public ResponseEntity<BaseResponse<List<ScheduleResponse>>> getSchedules(
            @RequestParam("schedule_id") List<Long> scheduleIds,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(),
                "일정 정보를 조회했습니다.", scheduleService.getSchedules(scheduleIds, userId)));
    }

    @PostMapping()
    @Operation(summary = "일정 생성", description = "새로운 일정을 추가합니다.")
    public ResponseEntity<BaseResponse<Void>> createSchedule(
            @RequestBody ScheduleRequest scheduleRequest,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        scheduleService.createSchedule(scheduleRequest, userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일정을 성공적으로 생성했습니다."));
    }

}
