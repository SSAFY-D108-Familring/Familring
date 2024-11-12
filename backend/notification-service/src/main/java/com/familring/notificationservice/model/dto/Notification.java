package com.familring.notificationservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private Long notificationId;
    private Long receiverUserId;
    private Long senderUserId;
    private String destinationId;
    private NotificationType notificationType;
    private String notificationTitle;
    private String notificationMessage;
    private boolean notificationIsRead;
    private LocalDateTime notificationCreatedAt;
    private LocalDateTime notificationReadAt;
}
