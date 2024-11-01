package com.familring.userservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @PatchMapping("/client/family/member")
    ResponseEntity<String> deleteFamilyMember(@RequestHeader("X-User-Id") Long userId);
}
