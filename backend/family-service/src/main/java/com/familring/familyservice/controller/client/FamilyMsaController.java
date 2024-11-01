package com.familring.familyservice.controller.client;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.service.family.FamilyService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/family")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class FamilyMsaController {

    private final FamilyService familyService;

    @GetMapping
    @Operation(summary = "회원의 가족 정보 조회 - Header", description = "Header의 토큰을 사용해 회원의 가족 정보를 조회")
    public ResponseEntity<BaseResponse<FamilyInfoResponse>> getFamilyInfo(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        FamilyInfoResponse response = familyService.getFamilyInfo(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 정보를 성공적으로 조회 했습니다.", response));
    }
}
