package com.familring.notificationservice.service;

import com.familring.notificationservice.config.firebase.FcmMessage;
import com.familring.notificationservice.config.firebase.FcmUtil;
import com.familring.notificationservice.exception.notification.NotFoundNotificationException;
import com.familring.notificationservice.model.dao.NotificationDao;
import com.familring.notificationservice.model.dto.Notification;
import com.familring.notificationservice.model.dto.NotificationType;
import com.familring.notificationservice.model.dto.request.MentionRequest;
import com.familring.notificationservice.model.dto.request.NotificationRequest;
import com.familring.notificationservice.model.dto.request.UnReadCountRequest;
import com.familring.notificationservice.model.dto.response.NotificationResponse;
import com.familring.notificationservice.model.dto.response.UserInfoResponse;
import com.familring.notificationservice.service.client.UserServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.N;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final FcmUtil fcmUtil;

    @Override
    public List<NotificationResponse> getUnReadNotification(Long userId) {
        // 1. 회원 정보 찾기
        UserInfoResponse user = userServiceFeignClient.getUser(userId).getData();
        log.info("[getAllNotification] 찾은 사용자 정보 userNickname={}", user.getUserNickname());

        // 2. 회원의 알림 찾기
        List<NotificationResponse> notificationResponseList = Optional.ofNullable(notificationDao.findNotificationByReceiverIdAndNotificationIsReadFalse(user.getUserId()))
                .orElse(Collections.emptyList()) // null일 경우 빈 리스트로 처리
                .stream()
                .map(notification -> NotificationResponse.builder()
                        .notificationId(notification.getNotificationId())
                        .receiverUserId(notification.getReceiverUserId())
                        .senderUserId(notification.getSenderUserId())
                        .destinationId(notification.getDestinationId())
                        .notificationType(notification.getNotificationType())
                        .notificationTitle(notification.getNotificationTitle())
                        .notificationMessage(notification.getNotificationMessage())
                        .notificationIsRead(notification.isNotificationIsRead())
                        .build())
                .collect(Collectors.toList());

            log.info("[getAllNotification] 안 읽은 알림 개수={}", notificationResponseList.size());

        return notificationResponseList;
    }

    @Override
    public void notificationToFamily(Long userId, MentionRequest mentionRequest) {
        log.info("[notificationToFamily] 알림 수신자={}, 알림 발신자={}", mentionRequest.getReceiverId(), userId);
        // 수신자에게 알림 전송
        UserInfoResponse usersList = userServiceFeignClient.getUser(mentionRequest.getReceiverId()).getData();
        FcmMessage.FcmDto fcmDto = fcmUtil.makeFcmDTO("사랑의 한마디", mentionRequest.getMention());
        fcmUtil.singleFcmSend(usersList, fcmDto);
        log.info("[notificationToFamily] 알림 전송 완료");
    }

    @Override
    @Transactional
    public void updateNotificationIsRead(Long userId, Long notificationId) {
        // 1. 해당하는 알림 찾기
        Notification notification = notificationDao.findNotificationByNotificationId(notificationId)
                .orElseThrow(() -> new NotFoundNotificationException());

        // 2. 알림 읽음 여부 수정
        notificationDao.updateNotificationIsReadByNotificationId(notificationId);
        log.info("[updateNotificationIsRead] 알림 읽음 완료={}", notification.isNotificationIsRead());
        
        // 3. 알림 수신자 안읽음 알림 개수 감소
        UnReadCountRequest unReadCountRequest = UnReadCountRequest.builder()
                .userId(userId)
                .amount(-1)
                .build();
        userServiceFeignClient.updateUserUnReadCount(unReadCountRequest);
        log.info("[updateNotificationIsRead] 알림 수신자 안읽음 알림 개수 감소 처리 완료");
    }

    @Override
    @Transactional
    public void alarmByFcm(NotificationRequest notificationRequest) {
        // 1. 알림 생성 -> 알림 수신 인원만큼 수행
        log.info("[alarmByFcm] 알림 수신 인원 수 {}명", notificationRequest.getReceiverUserIds().size());
        for(Long userId : notificationRequest.getReceiverUserIds()) {
            // 1-1. 알림 객체 생성
            Notification newNotification = Notification.builder()
                    .receiverUserId(userId)
                    .senderUserId(notificationRequest.getSenderUserId())
                    .destinationId(notificationRequest.getDestinationId())
                    .notificationType(notificationRequest.getNotificationType())
                    .notificationTitle(notificationRequest.getTitle())
                    .notificationMessage(notificationRequest.getMessage())
                    .build();

            notificationDao.insertNotification(newNotification);

            // 1-2. 알림 수신자 안읽음 알림 개수 증가
            UnReadCountRequest unReadCountRequest = UnReadCountRequest.builder()
                    .userId(userId)
                    .amount(1)
                    .build();
            userServiceFeignClient.updateUserUnReadCount(unReadCountRequest);
            log.info("[alarmByFcm] 알림 수신자 안읽음 알림 개수 증가 처리 완료");
        }

        // 2. 수신자들에게 알림 전송
        List<UserInfoResponse> usersList = userServiceFeignClient.getAllUser(notificationRequest.getReceiverUserIds()).getData();
        FcmMessage.FcmDto fcmDto = fcmUtil.makeFcmDTO(notificationRequest.getTitle(), notificationRequest.getMessage());
        fcmUtil.multiFcmSend(usersList, fcmDto);
        log.info("[alarmByFcm] 알림 전송 완료");
    }
}
