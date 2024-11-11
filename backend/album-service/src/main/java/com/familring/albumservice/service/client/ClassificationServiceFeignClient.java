package com.familring.albumservice.service.client;

import com.familring.albumservice.dto.client.FaceSimilarityRequest;
import com.familring.albumservice.dto.client.FaceSimilarityResponse;
import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("classification-service")
public interface ClassificationServiceFeignClient {

    @PostMapping("/face-recognition/classification")
    BaseResponse<List<FaceSimilarityResponse>> calculateSimilarity(@RequestBody FaceSimilarityRequest request);
}
