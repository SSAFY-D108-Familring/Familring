package com.familring.notificationservice.service;

import com.familring.notificationservice.model.dto.request.NotificationRequest;
import com.familring.notificationservice.model.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    // 알림 조회
    List<NotificationResponse> getAllNotification(Long userId);

    // 알림 읽음 상태 변경
    void updateNotificationIsRead(Long userId, Long notificationId);

    // 서비스 별 알림
    void alarmByFcm(NotificationRequest notificationRequest);
}
