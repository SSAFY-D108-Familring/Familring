package com.familring.notificationservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.notificationservice.model.dto.response.NotificationResponse;
import com.familring.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bouncycastle.asn1.x509.NoticeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Log4j2
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "알림 조회", description = "userId에 해당하는 알림 전체 조회")
    public ResponseEntity<BaseResponse<List<NotificationResponse>>> getAllNotification(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        List<NotificationResponse> responseList = notificationService.getAllNotification(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "회원의 알림을 성공적으로 조회했습니다.", responseList));
    }
}
