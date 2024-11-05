package com.familring.calendarservice.service.client;

import com.familring.calendarservice.dto.client.FamilyInfoResponse;
import com.familring.calendarservice.dto.client.UserInfoResponse;
import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    // 가족의 정보를 조회
    @GetMapping("/client/family")
    BaseResponse<FamilyInfoResponse> getFamilyInfo(@RequestParam Long userId);

    // 가족 구성원들을 조회
    @GetMapping("/client/family/member")
    BaseResponse<List<UserInfoResponse>> getFamilyMembers(@RequestParam Long userId);
}


