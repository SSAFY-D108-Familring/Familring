package com.familring.notificationservice.service;

import com.familring.notificationservice.model.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    // 알림 조회
    List<NotificationResponse> getAllNotification(Long userId);
}
