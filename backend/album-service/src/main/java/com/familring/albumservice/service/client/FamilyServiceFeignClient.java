package com.familring.albumservice.service.client;

import com.familring.albumservice.dto.client.FamilyInfoResponse;
import com.familring.albumservice.dto.client.UserInfoResponse;
import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    // 가족의 정보를 조회
    @GetMapping("/client/family")
    BaseResponse<FamilyInfoResponse> getFamilyInfo(@RequestParam Long userId);

    @GetMapping("/client/family/member")
    BaseResponse<List<UserInfoResponse>> getFamilyMemberList(@RequestParam Long userId);
}



