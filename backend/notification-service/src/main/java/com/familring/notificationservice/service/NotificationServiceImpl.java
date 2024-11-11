package com.familring.notificationservice.service;

import com.familring.notificationservice.model.dao.NotificationDao;
import com.familring.notificationservice.model.dto.Notification;
import com.familring.notificationservice.model.dto.response.NotificationResponse;
import com.familring.notificationservice.model.dto.response.UserInfoResponse;
import com.familring.notificationservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationServiceImpl implements NotificationService {

    private final NotificationDao notificationDao;
    private final UserServiceFeignClient userServiceFeignClient;

    @Override
    public List<NotificationResponse> getAllNotification(Long userId) {
        // 1. 회원 정보 찾기
        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();
        log.info("[getAllNotification] 찾은 사용자 정보 userNickname={}", user.getUserNickname());

        // 2. 회원의 알림 찾기
        List<NotificationResponse> notificationResponseList = notificationDao.findAllByReceiverId(user.getUserId()).stream()
                .map(notification -> NotificationResponse.builder()
                        .notificationId(notification.getNotificationId())
                        .receiverUserId(notification.getReceiverUserId())
                        .senderUserId(notification.getSenderUserId())
                        .notificationType(notification.getNotificationType())
                        .notificationTitle(notification.getNotificationTitle())
                        .notificationMessage(notification.getNotificationMessage())
                        .notificationIsRead(notification.isNotificationIsRead())
                        .build())
                .collect(Collectors.toList());

        return notificationResponseList;
    }
}
