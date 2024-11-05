package com.familring.questionservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.questionservice.dto.request.QuestionAnswerCreateRequest;
import com.familring.questionservice.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/answers")
    @Operation(summary = "랜덤 질문 답변 작성", description = "랜덤 질문 답변 작성 (내용만 입력)")
    public ResponseEntity<BaseResponse<Void>> createTimeCapsule(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId, @RequestBody QuestionAnswerCreateRequest questionAnswerCreateRequest) {
        questionService.createQuestionAnswer(userId, questionAnswerCreateRequest);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "랜덤 질문 답변 작성에 성공했습니다."));
    }

}
