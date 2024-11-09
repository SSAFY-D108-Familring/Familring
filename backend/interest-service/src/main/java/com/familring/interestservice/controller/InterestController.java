package com.familring.interestservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.interestservice.dto.request.InterestAnswerCreateRequest;
import com.familring.interestservice.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interests")
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    @PostMapping("/answers")
    @Operation(summary = "관심사 답변 작성", description = "관심사 답변 작성 (내용만 입력)")
    public ResponseEntity<BaseResponse<Void>> createInterestAnswer(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId, @RequestBody InterestAnswerCreateRequest interestAnswerCreateRequest) {
        interestService.createInterestAnswer(userId, interestAnswerCreateRequest);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 답변 작성에 성공했습니다."));
    }

    @PatchMapping("/answers/{interest-id}")
    @Operation(summary = "관심사 답변 수정", description = "관심사 답변 수정 (내용만 입력), 관심사 ID 필수")
    public ResponseEntity<BaseResponse<Void>> updateInterestAnswer(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userID, @PathVariable(name="interest-id") Long interestId, @RequestBody InterestAnswerCreateRequest interestAnswerCreateRequest) {
        interestService.updateInterestAnswer(userID, interestId, interestAnswerCreateRequest);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 답변 수정에 성공했습니다."));
    }

    

}
