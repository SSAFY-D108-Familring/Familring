package com.familring.interestservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.interestservice.dto.request.InterestAnswerCreateRequest;
import com.familring.interestservice.dto.request.InterestMissionCreatePeriodRequest;
import com.familring.interestservice.dto.response.*;
import com.familring.interestservice.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @GetMapping("/answers/mine")
    @Operation(summary = "내가 작성한 관심사 조회", description = "내가 작성했던 관심사 조회 (수정하는 화면에서 내가 작성한 관심사 조회하는 용도)")
    public ResponseEntity<BaseResponse<InterestAnswerMineResponse>> getInterestAnswerMine (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        InterestAnswerMineResponse response = interestService.getInterestAnswerMine(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "내가 작성했던 관심사 조회에 성공했습니다.", response));
    }

    @GetMapping("/answers/selected")
    @Operation(summary = "선정된 관심사 조회", description = "선정된 관심사 조회 (답변을 작성한 가족 구성원 중에 랜덤으로 돌려서 선정)")
    public ResponseEntity<BaseResponse<InterestAnswerSelectedResponse>> getInterestAnswerSelected (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        InterestAnswerSelectedResponse response = interestService.getInterestAnswerSelected(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "선정된 관심사 조회에 성공했습니다.", response));
    }

    @PostMapping("/missions/period")
    @Operation(summary = "관심사 체험 인증 기한 설정", description = "관심사 체험하는 기한 설정 (LocalDate 날짜만 입력)")
    public ResponseEntity<BaseResponse<Void>> setInterestMissionPeriod(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userID, @RequestBody InterestMissionCreatePeriodRequest interestMissionCreatePeriodRequest) {
        interestService.setInterestMissionPeriod(userID, interestMissionCreatePeriodRequest);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 체험 인증 기한 설정에 성공했습니다."));
    }

    @GetMapping("/missions/period")
    @Operation(summary = "관심사 체험 인증 남은 기간 조회", description = "관심사 체험하는 기간 얼마나 남았는지 조회")
    public ResponseEntity<BaseResponse<Integer>> getInterestMissionDate (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        int response = interestService.getInterestMissionDate(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 체험 인증 남은 기간 조회에 성공했습니다.", response));
    }

    @PostMapping(path = "/missions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "관심사 체험 인증 게시글 작성", description = "관심사 체험 인증 게시글 작성 (이미지만 입력)")
    public ResponseEntity<BaseResponse<Void>> createInterestMission(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userID, @RequestPart("image") MultipartFile image) {
        interestService.createInterestMission(userID, image);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 체험 인증 게시글 작성에 성공했습니다."));
    }

    @GetMapping("/missions")
    @Operation(summary = "관심사 체험 인증 목록 조회", description = "관심사 체험했던 구성원 목록 조회")
    public ResponseEntity<BaseResponse<InterestMissionListResponse>> getInterestMissionList (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        InterestMissionListResponse response = interestService.getInterestMissionList(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 체험 인증 목록 조회에 성공했습니다.", response));
    }

    @GetMapping()
    @Operation(summary = "관심사 전체 목록 조회", description = "그동안 선정됐던 관심사 목록 조회")
    public ResponseEntity<BaseResponse<InterestListResponse>> getInterestList (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId, @RequestParam int pageNo) {
        InterestListResponse response = interestService.getInterestList(userId, pageNo);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 전체 목록 조회에 성공했습니다.", response));
    }

    @GetMapping("/{interest-id}")
    @Operation(summary = "관심사 상세보기", description = "선정됐던 관심사 상세보기")
    public ResponseEntity<BaseResponse<List<InterestDetailResponse>>> getInterestDetail (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId, @PathVariable(name="interest-id") Long interestId) {
        List<InterestDetailResponse> response = interestService.getInterestDetail(userId, interestId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 전체 목록 조회에 성공했습니다.", response));
    }

    @GetMapping("/status")
    @Operation(summary = "관심사 상태 관리", description = "0, 1, 2 로 상태 구분")
    public ResponseEntity<BaseResponse<Integer>> getInterestStatus(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        int response = interestService.getInterestStatus(userId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "관심사 상태 관리 조회에 성공했습니다.", response));
    }
}
