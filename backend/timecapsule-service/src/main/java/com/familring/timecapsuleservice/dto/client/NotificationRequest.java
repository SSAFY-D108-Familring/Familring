package com.familring.timecapsuleservice.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private NotificationType notificationType;      // 알림 발생지
    private List<Long> receiverUserIds; // 알림 받는 사람들 Id
    private Long senderUserId;          // 알림 전송하는 사람 Id
    private String destinationId;       // 알림으로 이동할 Id
    private String title;               // 알림 title
    private String message;             // 알림 보낼 message
}
