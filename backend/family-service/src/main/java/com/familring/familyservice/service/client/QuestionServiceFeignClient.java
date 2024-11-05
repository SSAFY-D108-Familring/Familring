package com.familring.familyservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "question-service")
public interface QuestionServiceFeignClient {
    @PostMapping()
    BaseResponse<Void> initializeQuestionFamily (@RequestBody Long familyId);
}
