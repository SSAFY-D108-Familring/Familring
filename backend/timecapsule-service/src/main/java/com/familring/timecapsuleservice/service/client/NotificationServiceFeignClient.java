package com.familring.timecapsuleservice.service.client;

import com.familring.common_module.dto.BaseResponse;
import com.familring.timecapsuleservice.dto.client.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="notification-service")
public interface NotificationServiceFeignClient {
    @PostMapping("/client/notifications/fcm")
    BaseResponse<Void> alarmByFcm(@RequestBody NotificationRequest notificationRequest);
}
