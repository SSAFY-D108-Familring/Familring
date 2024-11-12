package com.familring.notificationservice.controller.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.notificationservice.model.dto.request.NotificationRequest;
import com.familring.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client/notifications")
@RequiredArgsConstructor
@Log4j2
@Hidden
public class NotificationClientController {

    private final NotificationService notificationService;

    @PostMapping("/fcm")
    public ResponseEntity<BaseResponse<Void>> alarmByFcm(@RequestBody NotificationRequest notificationRequest) {
        notificationService.alarmByFcm(notificationRequest);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "알림이 성공적으로 전송되었습니다."));
    }
}
