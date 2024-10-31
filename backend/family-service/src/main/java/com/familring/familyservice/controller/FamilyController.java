package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.request.FamilyCreateRequest;
import com.familring.familyservice.model.dto.request.FamilyJoinRequest;
import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.model.dto.response.UserInfoResponse;
import com.familring.familyservice.service.FamilyService;
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
    public ResponseEntity getFamilyInfo(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        FamilyInfoResponse response = familyService.getFamilyInfo(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/code")
    public ResponseEntity getFamilyCode(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        String response = familyService.getFamilyCode(userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/member")
    public ResponseEntity getFamilyMemberList(@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        List<UserInfoResponse> responseList = familyService.getFamilyMemberList(userId);

        return ResponseEntity.ok(responseList);
    }

    @PostMapping
    public ResponseEntity createFamily
            (@RequestHeader("X-User-Id") Long userId,
             @RequestBody FamilyCreateRequest familyCreateRequest) {
        FamilyInfoResponse response = familyService.createFamily(userId, familyCreateRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity joinFamilyMember
            (@RequestHeader("X-User-Id") Long userId,
             @RequestBody FamilyJoinRequest familyJoinRequest) {
        String response = familyService.joinFamilyMember(userId, familyJoinRequest);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/member")
    public ResponseEntity deleteFamilyMember (@Parameter(hidden = true) @RequestHeader("X-User-Id") Long userId) {
        String response = familyService.deleteFamilyMember(userId);

        return ResponseEntity.ok(response);
    }
}
