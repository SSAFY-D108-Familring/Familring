package com.familring.familyservice.controller;

import com.familring.common_service.dto.BaseResponse;
import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import com.familring.familyservice.model.dto.request.FamilyJoinRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.FamilyService;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
@Log4j2
@Tag(name = "가족 컨트롤러", description = "가족관련 기능 수행")
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    @Operation(summary = "회원의 가족 정보 조회 - Header", description = "Header의 토큰을 사용해 회원의 가족 정보를 조회")
    public ResponseEntity<BaseResponse<FamilyInfoResponse>> getFamilyInfo(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        FamilyInfoResponse response = familyService.getFamilyInfo(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 정보를 성공적으로 조회 했습니다.", response));
    }

    @GetMapping("/code")
    @Operation(summary = "가족 코드 조회", description = "Header의 토큰을 사용해 회원의 가족 정보 중 가족 코드를 조회")
    public ResponseEntity<BaseResponse<String>> getFamilyCode(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        String response = familyService.getFamilyCode(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 코드를 성공적으로 조회 했습니다.", response));
    }

    @GetMapping("/member")
    @Operation(summary = "가족 구성원 조회", description = "Header의 토큰을 사용해 회원의 가족 정보 중 가족 코드를 조회")
    public ResponseEntity<BaseResponse<List<UserInfoResponse>>> getFamilyMemberList(@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        List<UserInfoResponse> responseList = familyService.getFamilyMemberList(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "가족 구성원들을 성공적으로 조회 했습니다.", responseList));
    }

    @PostMapping
    @Operation(summary = "가족 생성", description = "가족 테이블을 생성")
    public ResponseEntity<BaseResponse<FamilyInfoResponse>> createFamily
            (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        FamilyInfoResponse response = familyService.createFamily(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.CREATED.value(), "가족을 성공적으로 생성했습니다.", response));
    }

    @PostMapping("/join")
    @Operation(summary = "가족 구성원 추가", description = "가족코드에 해당하는 가족에 가족 구성원 추가")
    public ResponseEntity<BaseResponse<String>> joinFamilyMember
            (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
             @RequestBody FamilyJoinRequest familyJoinRequest) {
        String response = familyService.joinFamilyMember(userId, familyJoinRequest);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "해당 회원을 성공적으로 가족 구성원에 추가했습니다.", response));
    }

    @PatchMapping("/member")
    @Operation(summary = "가족 구성원 제거", description = "가족 구성원에서 Header의 토큰에 해당하는 회원 삭제")
    public ResponseEntity<BaseResponse<String>> deleteFamilyMember (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String response = familyService.deleteFamilyMember(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "해당 회원을 성공적으로 가족 구성원에 제거했습니다.", response));
    }
}
