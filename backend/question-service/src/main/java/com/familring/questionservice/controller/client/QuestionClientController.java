package com.familring.questionservice.controller.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.questionservice.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client/questions")
@RequiredArgsConstructor
public class QuestionClientController {

    private final QuestionService questionService;

    @PostMapping("/initial")
    public ResponseEntity<BaseResponse<Void>> initializeQuestionFamily(@RequestBody Long familyId) {
        questionService.initializeQuestionFamily(familyId);
        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "첫 번째 랜덤 질문 생성에 성공했습니다."));
    }

}
