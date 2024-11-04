package com.familring.timecapsuleservice.controller;

import com.familring.common_service.dto.BaseResponse;
import com.familring.timecapsuleservice.dto.request.TimeCapsuleAnswerCreateRequest;
import com.familring.timecapsuleservice.dto.request.TimeCapsuleCreateRequest;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleListResponse;
import com.familring.timecapsuleservice.dto.response.TimeCapsuleStatusResponse;
import com.familring.timecapsuleservice.service.TimeCapsuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timecapsules")
public class TimeCapsuleController {

    private final TimeCapsuleService timeCapsuleService;

    @GetMapping("/status")
    @Operation(summary = "타입 캡슐 상태 관리", description = "타입 캡슐 3가지 상태에 따라 분기 처리")
    public ResponseEntity<BaseResponse<TimeCapsuleStatusResponse>> getTimeCapsuleStatus(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        TimeCapsuleStatusResponse response = timeCapsuleService.getTimeCapsuleStatus(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "타입 캡슐 상태를 성공적으로 조회 했습니다.", response));
    }

    @PostMapping()
    @Operation(summary = "타입 캡슐 생성", description = "타임 캡슐 생성하기 (날짜만 입력)")
    public ResponseEntity<BaseResponse<Void>> createTimeCapsule(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId, @RequestBody TimeCapsuleCreateRequest timeCapsuleCreateRequest) {
        timeCapsuleService.createTimeCapsule(userId, timeCapsuleCreateRequest);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "타입 캡슐 생성에 성공했습니다."));
    }

    @PostMapping("/answers")
    @Operation(summary = "타입 캡슐 답변 작성", description = "타임 캡슐 답변 작성하기 (내용만 입력)")
    public ResponseEntity<BaseResponse<Void>> createTimeCapsuleAnswer(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId, @RequestBody TimeCapsuleAnswerCreateRequest timeCapsuleAnswerCreateRequest) {
        timeCapsuleService.createTimeCapsuleAnswer(userId, timeCapsuleAnswerCreateRequest);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "타입 캡슐 답변 작성을 성공했습니다."));
    }

    @GetMapping()
    @Operation(summary = "타입 캡슐 목록 조회", description = "타임 캡슐 목록 조회 (답변 목록 조회까지 같이)")
    public ResponseEntity<BaseResponse<TimeCapsuleListResponse>> getTimeCapsuleList(
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId,
            @RequestParam("pageNo") int pageNo) {
        TimeCapsuleListResponse response = timeCapsuleService.getTimeCapsuleList(userId, pageNo);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "타임 캡슐 목록을 성공적으로 조회 했습니다.", response));
    }
}
