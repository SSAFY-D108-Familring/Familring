package com.familring.calendarservice.service.client;

import com.familring.calendarservice.dto.client.NotificationRequest;
import com.familring.common_module.dto.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="notification-service")
public interface NotificationServiceFeignClient {
    @PostMapping("/client/notifications/fcm")
    BaseResponse<Void> alarmByFcm(@RequestBody NotificationRequest notificationRequest);
}
