package com.familring.timecapsuleservice.service.client;

import com.familring.common_service.dto.BaseResponse;
import com.familring.timecapsuleservice.dto.client.FamilyDto;
import com.familring.timecapsuleservice.dto.client.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @GetMapping("/client/family")
    BaseResponse<FamilyDto> getFamilyInfo(@RequestParam Long userId);

    @GetMapping("/client/family/member")
    BaseResponse<List<UserInfoResponse>> getFamilyMemberList(@RequestParam Long userId);
}
