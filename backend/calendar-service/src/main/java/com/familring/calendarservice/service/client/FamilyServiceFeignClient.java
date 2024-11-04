package com.familring.calendarservice.service.client;

import com.familring.calendarservice.service.client.dto.FamilyInfoResponse;
import com.familring.calendarservice.service.client.dto.UserInfoResponse;
import com.familring.common_service.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    // 가족의 정보를 조회
    @GetMapping("/client/family")
    BaseResponse<FamilyInfoResponse> getFamilyInfo(Long userId);

    // 가족 구성원들을 조회
    @GetMapping("/client/family/member")
    BaseResponse<List<UserInfoResponse>> getFamilyMembers(Long userId);
}


