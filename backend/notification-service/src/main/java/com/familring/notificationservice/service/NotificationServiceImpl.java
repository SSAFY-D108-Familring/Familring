package com.familring.notificationservice.service;

import com.familring.notificationservice.exception.notification.NotFoundNotificationException;
import com.familring.notificationservice.model.dao.NotificationDao;
import com.familring.notificationservice.model.dto.Notification;
import com.familring.notificationservice.model.dto.response.NotificationResponse;
import com.familring.notificationservice.model.dto.response.UserInfoResponse;
import com.familring.notificationservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        List<NotificationResponse> notificationResponseList = Optional.ofNullable(notificationDao.findAllByReceiverId(user.getUserId()))
                .orElse(Collections.emptyList()) // null일 경우 빈 리스트로 처리
                .stream()
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

    @Override
    public void updateNotificationIsRead(Long userId, Long notificationId) {
        // 1. 해당하는 알림 찾기
        Notification notification = notificationDao.findNotificationByNotificationId(notificationId)
                .orElseThrow(() -> new NotFoundNotificationException());

        // 2. 알림 읽음 여부 수정
        notificationDao.updateNotificationIsReadByNotificationId(notificationId);
        log.info("[] 알림 읽음 완료={}", notification.isNotificationIsRead());
    }
}
