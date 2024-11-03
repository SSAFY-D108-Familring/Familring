package com.familring.calendarservice.controller;

import com.familring.calendarservice.service.DailyService;
import com.familring.calendarservice.service.ScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendars")
@RequiredArgsConstructor
@Tag(name = "달력 컨트롤러", description = "일정, 일상 관리")
public class CalendarController {

    private final DailyService dailyService;
    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<String> getSchedulesAndDailiesByMonth(
            @RequestParam int month,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        // 2. 일정 조회는 유저 아이디로 가족 id 조회해서 디비에서 긁어오기
        dailyService.getDailiesByMonth(month, userId);

        // 3. 일상 조회는 유저 아이디로 가족 구성원 조회해서 디비에서 그 구성원들의 게시물 긁어오기
        scheduleService.getSchedulesByMonth(month, userId);


        // 일정과 일상을 작성 날짜와 함께 줘야함
    }

}
