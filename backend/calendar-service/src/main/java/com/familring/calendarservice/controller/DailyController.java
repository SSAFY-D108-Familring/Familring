package com.familring.calendarservice.controller;

import com.familring.calendarservice.dto.response.DailyResponse;
import com.familring.calendarservice.service.DailyService;
import com.familring.common_module.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/calendars/dailies")
@RequiredArgsConstructor
@Tag(name = "일상 컨트롤러", description = "일상 관리")
public class DailyController {

    private final DailyService dailyService;

    @GetMapping()
    @Operation(summary = "다중 일상 조회", description = "일상들의 정보를 조회합니다.")
    public ResponseEntity<BaseResponse<List<DailyResponse>>> getDailies(
            @RequestParam("daily_id") List<Long> dailyIds,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(),
                "일상 정보를 조회했습니다.", dailyService.getDailies(dailyIds, userId)));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "일상 생성", description = "일상을 생성합니다.")
    public ResponseEntity<BaseResponse<Void>> createDaily(
            @RequestPart("content") String content,
            @RequestPart("image") MultipartFile image,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        dailyService.createDaily(content, image, userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일상을 성공적으로 업로드했습니다."));
    }

    @PatchMapping(path = "/{daily_id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "일상 수정", description = "일상을 수정합니다.")
    public ResponseEntity<BaseResponse<Void>> updateDaily(
            @RequestPart("content") String content,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @PathVariable("daily_id") Long dailyId,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        dailyService.updateDaily(content, image, dailyId, userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일상을 성공적으로 업로드했습니다."));
    }

    @DeleteMapping("/{daily_id}")
    @Operation(summary = "일상 삭제", description = "일상을 삭제합니다.")
    public ResponseEntity<BaseResponse<Void>> deleteDaily(
            @PathVariable("daily_id") Long dailyId,
            @Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId
    ) {
        dailyService.deleteDaily(dailyId, userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "일상을 성공적으로 삭제했습니다."));
    }
}
