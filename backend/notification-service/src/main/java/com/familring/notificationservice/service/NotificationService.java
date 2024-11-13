package com.familring.notificationservice.service;

import com.familring.notificationservice.model.dto.request.MentionRequest;
import com.familring.notificationservice.model.dto.request.NotificationRequest;
import com.familring.notificationservice.model.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    // 안읽은 알림만 조회
    List<NotificationResponse> getUnReadNotification(Long userId);

    // 가족에게 알림 전송
    void notificationToFamily(Long userId, MentionRequest mentionRequest);

    // 알림 읽음 상태 변경
    void updateNotificationIsRead(Long userId, Long notificationId);

    // 서비스 별 알림
    void alarmByFcm(NotificationRequest notificationRequest);
}
