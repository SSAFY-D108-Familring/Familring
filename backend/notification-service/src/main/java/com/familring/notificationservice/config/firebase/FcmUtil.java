package com.familring.notificationservice.config.firebase;

import com.familring.notificationservice.model.dto.response.UserInfoResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class FcmUtil {

    // 단일 사용자에게 FCM 메시지를 비동기로 전송하는 메서드
    @Async("taskExecutor") // 비동기 처리
    public void singleFcmSend(UserInfoResponse user, FcmMessage.FcmDto fcmDTO) {
        String fcmToken = user.getUserFcmToken(); // 사용자 FCM 토큰 가져오기

        // FCM 토큰이 유효한 경우 메시지 생성 및 전송
        if (fcmToken != null && !fcmToken.isEmpty()) {
            Message message = makeMessage(fcmDTO.getTitle(), fcmDTO.getBody(), fcmToken); // 메시지 생성
            sendMessage(message); // 메시지 전송
        }
    }

    // 여러 사용자에게 FCM 메시지를 비동기로 전송하는 메서드
    @Async("taskExecutor") // 비동기 처리
    public void multiFcmSend(List<UserInfoResponse> users, FcmMessage.FcmDto fcmDTO) {
        users.forEach(user -> singleFcmSend(user, fcmDTO));
    }


    // FCM 메시지를 생성하는 메서드
    public Message makeMessage(String title, String body, String token) { // FcmDTO의 title, body 사용
        // 알림 객체 생성
        Notification notification =
                Notification.builder()
                        .setTitle(title)    // 제목 설정
                        .setBody(body)      // 본문 설정
                        .build();

        // 메시지 객체 생성
        return Message.builder()
                .setNotification(notification)  // 알림 설정
                .setToken(token)                // FCM 토큰 설정
                .putData("title", title)        // 데이터에 제목 추가
                .putData("body", body)          // 데이터에 본문 추가
                .build();
    }

    // FCM 메시지를 전송하는 메서드
    public void sendMessage(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message); // 메시지 전송
        } catch (FirebaseMessagingException e) {
            log.error("FCM send error");    // 전송 오류 로그
        }
    }

    public FcmMessage.FcmDto makeFcmDTO(String title, String body) {
        return FcmMessage.FcmDto.builder()
                .title(title)
                .body(body)
                .build();
    }
}

