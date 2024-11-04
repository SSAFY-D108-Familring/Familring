package com.familring.familyservice.controller.client;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.model.dto.request.FamilyStatusRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.family.FamilyService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/family")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class FamilyClientController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity<BaseResponse<FamilyInfoResponse>> getFamilyInfo(Long userId) {
        FamilyInfoResponse response = familyService.getFamilyInfo(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 정보를 성공적으로 조회 했습니다.", response));
    }

    @GetMapping("/member")
    public ResponseEntity<BaseResponse<List<UserInfoResponse>>> getFamilyMemberList(Long userId) {
        List<UserInfoResponse> responseList = familyService.getFamilyMemberList(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 구성원들을 성공적으로 조회 했습니다.", responseList));
    }

    @PutMapping("/member")
    public ResponseEntity<BaseResponse<String>> deleteFamilyMember (Long userId) {
        String response = familyService.deleteFamilyMember(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "해당 회원을 성공적으로 가족 구성원에 제거했습니다.", response));
    }

    @PatchMapping("/status")
    public ResponseEntity<BaseResponse<String>> updateFamilyStatus(FamilyStatusRequest familyStatusRequest) {
        familyService.updateFamilyStatus(familyStatusRequest);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 상태를 성공적으로 변경했습니다."));
    }
}
