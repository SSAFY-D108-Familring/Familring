package com.familring.notificationservice.config.firebase;

import com.familring.notificationservice.model.dto.NotificationType;
import com.familring.notificationservice.model.dto.response.UserInfoResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
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

        if (fcmToken != null && !fcmToken.isEmpty()) {
            Message message = makeMessage(fcmDTO.getTitle(), fcmDTO.getBody(), fcmToken, fcmDTO.getType()); // 메시지 생성
            sendMessage(message); // 메시지 전송
        } else if (fcmToken == null) {
            log.info("[singleFcmSend] userId={}에게 FCM 토큰이 없습니다.", user.getUserFcmToken());
        }
    }

    // 여러 사용자에게 FCM 메시지를 비동기로 전송하는 메서드
    @Async("taskExecutor") // 비동기 처리
    public void multiFcmSend(List<UserInfoResponse> users, FcmMessage.FcmDto fcmDTO) {
        users.forEach(user -> singleFcmSend(user, fcmDTO));
    }

    // FCM 메시지를 생성하는 메서드
    public Message makeMessage(String title, String body, String token, String type) { // FcmDTO의 title, body 사용
        // 메시지 객체 생성
        return Message.builder()
                .setToken(token)                // FCM 토큰 설정
                .putData("title", title)        // 데이터에 제목 추가
                .putData("body", body)          // 데이터에 본문 추가
                .putData("type", type)
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

    public FcmMessage.FcmDto makeFcmDTO(String title, String body, String type) {
        return FcmMessage.FcmDto.builder()
                .title(title)
                .body(body)
                .type(type)
                .build();
    }
}

