package com.familring.calendarservice.controller;

import com.familring.calendarservice.service.DailyService;
import com.familring.common_service.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/calendars/dailies")
@RequiredArgsConstructor
@Tag(name = "일상 컨트롤러", description = "일상 관리")
public class DailyController {

    private final DailyService dailyService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "일상 생성", description = "일상을 수정합니다.")
    public ResponseEntity<BaseResponse<Void>> createDaily(
            @RequestParam("content") String content,  // 일반 텍스트는 RequestParam으로 받기
            @RequestParam("image") MultipartFile image,  // 파일도 RequestParam으로 받을 수 있음
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        dailyService.createDaily(content, image, userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일상을 성공적으로 업로드했습니다."));
    }
}
