package com.familring.userservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @PatchMapping("/family/member")
    ResponseEntity<String> deleteFamilyMember(@RequestHeader("X-User-Id") Long userId);
}
