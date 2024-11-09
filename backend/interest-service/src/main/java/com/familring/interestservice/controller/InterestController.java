package com.familring.interestservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.interestservice.dto.request.InterestAnswerCreateRequest;
import com.familring.interestservice.dto.response.InterestAnswerListResponse;
import com.familring.interestservice.dto.response.InterestAnswerMineResponse;
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

    @GetMapping("/answers/status")
    @Operation(summary = "관심사 답변 작성 유무", description = "관심사 답변을 했는지 안했는지 boolean 값으로 전달 (답변 했으면 true, 답변 안했으면 false")
    public ResponseEntity<BaseResponse<Boolean>> getQuestion(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        boolean response = interestService.getInterestAnswerStatus(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 답변 작성 유무 조회에 성공했습니다.", response));
    }

    @GetMapping("/answers")
    @Operation(summary = "관심사 답변 목록 조회 (가족)", description = "가족들이 작성한 관심사 답변 목록 조회")
    public ResponseEntity<BaseResponse<InterestAnswerListResponse>> getInterestAnswerList (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        InterestAnswerListResponse response = interestService.getInterestAnswerList(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족들의 관심사 답변 목록 조회에 성공했습니다.", response));
    }

    @GetMapping("/interest/answers/mine")
    @Operation(summary = "내가 작성한 관심사 조회", description = "내가 작성했던 관심사 조회 (수정하는 화면에서 내가 작성한 관심사 조회하는 용도)")
    public ResponseEntity<BaseResponse<InterestAnswerMineResponse>> getInterestAnswerMine (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        InterestAnswerMineResponse response = interestService.getInterestAnswerMine(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "내가 작성했던 관심사 조회에 성공했습니다.", response));
    }

}
