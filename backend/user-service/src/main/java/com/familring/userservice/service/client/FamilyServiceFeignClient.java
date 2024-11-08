package com.familring.userservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.userservice.model.dto.request.FamilyJoinRequest;
import com.familring.userservice.model.dto.response.FamilyInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @PutMapping("/client/family/member")
    BaseResponse<String> deleteFamilyMember(@RequestParam Long userId);

    @PostMapping("/client/family")
    BaseResponse<FamilyInfoResponse> createFamily(@RequestParam Long userId);

    @PostMapping("/client/family/join")
    BaseResponse<String> joinFamilyMember(@RequestBody FamilyJoinRequest familyJoinRequest);
}
