package com.familring.questionservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.questionservice.dto.client.Family;
import com.familring.questionservice.dto.client.FamilyStatusRequest;
import com.familring.questionservice.dto.client.UserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "family-service")
public interface FamilyServiceFeignClient {
    @GetMapping("/client/family")
    BaseResponse<Family> getFamilyInfo(@RequestParam Long userId);

    @GetMapping("/client/family/member")
    BaseResponse<List<UserInfoResponse>> getFamilyMemberList(@RequestParam Long userId);

    @GetMapping("/client/family/all")
    BaseResponse<List<Long>> getAllFamilyId();

    @GetMapping("/client/family/member/info")
    BaseResponse<List<UserInfoResponse>> getFamilyMemberListByFamilyId(@RequestParam Long familyId);

    @PutMapping("/client/family/status")
    void updateFamilyStatus(@RequestBody FamilyStatusRequest familyStatusRequest);
}
