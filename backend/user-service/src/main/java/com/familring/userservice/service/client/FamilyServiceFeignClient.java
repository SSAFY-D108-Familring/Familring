package com.familring.userservice.service.client;

import com.familring.common_service.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @PatchMapping("/client/family/member")
    BaseResponse<String> deleteFamilyMember(@RequestHeader("X-User-Id") Long userId);
}
