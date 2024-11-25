package com.familring.notificationservice.controller;

import com.familring.common_module.dto.BaseResponse;
import com.familring.notificationservice.model.dto.request.MentionRequest;
import com.familring.notificationservice.model.dto.response.NotificationResponse;
import com.familring.notificationservice.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Tag(name = "알림 컨트롤러", description = "알림관련 기능 수행")
@RequiredArgsConstructor
@Log4j2
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "안읽은 알림 조회", description = "userId에 해당하는 안읽은 알림 조회")
    public ResponseEntity<BaseResponse<List<NotificationResponse>>> getUnReadNotification(@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId) {
        List<NotificationResponse> responseList = notificationService.getUnReadNotification(userId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "회원의 알림을 성공적으로 조회했습니다.", responseList));
    }

    @PostMapping("/mention")
    @Operation(summary = "가족에게 한마디", description = "receiverId에 해당하는 사람에게 userId에 해당하는 사람이 알림을 전송")
    public ResponseEntity<BaseResponse<Void>> notificationToFamily
            (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId,
             @RequestBody MentionRequest mentionRequest) {
        notificationService.notificationToFamily(userId, mentionRequest);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "회원에게 알림이 성공적으로 전송되었습니다."));
    }

    @PatchMapping("/{notificationId}")
    @Operation(summary = "알림 읽음 처리", description = "notificationId에 해당하는 알림 읽음 처리")
    public ResponseEntity<BaseResponse<Void>> updateNotificationIsRead
            (@Parameter(hidden = true) @RequestHeader("X-User-ID") Long userId,
             @PathVariable("notificationId") Long notificationId) {
        notificationService.updateNotificationIsRead(userId, notificationId);

        return ResponseEntity.ok(BaseResponse.create(HttpStatus.OK.value(), "알림을 성공적으로 읽음 처리했습니다."));
    }
}
