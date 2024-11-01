package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import com.familring.familyservice.model.dto.request.FamilyJoinRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
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
    public ResponseEntity getFamilyInfo(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        FamilyInfoResponse response = familyService.getFamilyInfo(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/code")
    @Operation(summary = "가족 코드 조회", description = "Header의 토큰을 사용해 회원의 가족 정보 중 가족 코드를 조회")
    public ResponseEntity getFamilyCode(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        String response = familyService.getFamilyCode(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/member")
    @Operation(summary = "가족 구성원 조회", description = "Header의 토큰을 사용해 회원의 가족 정보 중 가족 코드를 조회")
    public ResponseEntity getFamilyMemberList(@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        List<UserInfoResponse> responseList = familyService.getFamilyMemberList(userId);

        return ResponseEntity.ok(responseList);
    }

    @PostMapping
    @Operation(summary = "가족 생성", description = "가족 테이블을 생성")
    public ResponseEntity createFamily
            (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
             @RequestBody FamilyCreateRequest familyCreateRequest) {
        FamilyInfoResponse response = familyService.createFamily(userId, familyCreateRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    @Operation(summary = "가족 구성원 추가", description = "가족코드에 해당하는 가족에 가족 구성원 추가")
    public ResponseEntity joinFamilyMember
            (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId,
             @RequestBody FamilyJoinRequest familyJoinRequest) {
        String response = familyService.joinFamilyMember(userId, familyJoinRequest);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/member")
    @Operation(summary = "가족 구성원 제거", description = "가족 구성원에서 Header의 토큰에 해당하는 회원 삭제")
    public ResponseEntity deleteFamilyMember (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String response = familyService.deleteFamilyMember(userId);

        return ResponseEntity.ok(response);
    }
}
