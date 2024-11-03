package com.familring.calendarservice.controller;

import com.familring.calendarservice.service.ScheduleService;
import com.familring.common_service.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendars/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;

//    @GetMapping()
//    @Operation(summary = "다중 일정 조회", description = "일정들의 세부 정보를 조회합니다.")
//    public ResponseEntity<BaseResponse> getSchedules(@RequestParam("schedule_id") List<Long> scheduleIds){
//        scheduleService.getSchedules(scheduleIds);
//    }
}
