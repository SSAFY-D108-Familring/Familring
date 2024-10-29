package com.familring.familyservice.controller;

import com.familring.familyservice.model.dto.response.FamilyInfoResponse;
import com.familring.familyservice.service.FamilyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/family")
@RequiredArgsConstructor
@Log4j2
//@Tag(name = "가족 컨트롤러", description = "가족관련 기능 수행")
public class FamilyController {

    private final FamilyService familyService;

    @GetMapping
    public ResponseEntity getFamilyInfo(@RequestHeader("Authorization") String token) {
        FamilyInfoResponse response = familyService.getFamilyInfo(token);

        return ResponseEntity.ok(response);
    }
}
