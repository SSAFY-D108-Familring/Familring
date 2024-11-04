package com.familring.timecapsuleservice.controller;

import com.familring.common_service.dto.BaseResponse;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleStatusResponse;
import com.familring.timecapsuleservice.service.TimeCapsuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timecapsules")
public class TimeCapsuleController {

    private final TimeCapsuleService timeCapsuleService;

    @GetMapping("/status")
    @Operation(summary = "타입 캡슐 상태 관리", description = "타입 캡슐 3가지 상태에 따라 분기 처리")
    public ResponseEntity<BaseResponse<TimeCapsuleStatusResponse>> getFamilyInfo(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        TimeCapsuleStatusResponse response = timeCapsuleService.getTimeCapsuleStatus(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "타입 캡슐 상태를 성공적으로 조회 했습니다.", response));
    }

}
