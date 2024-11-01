package com.familring.userservice.service.client;

import com.familring.common_service.dto.BaseResponse;
import com.familring.userservice.config.feign.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @PutMapping("/client/family/member")
    BaseResponse<String> deleteFamilyMember(Long userId);
}
