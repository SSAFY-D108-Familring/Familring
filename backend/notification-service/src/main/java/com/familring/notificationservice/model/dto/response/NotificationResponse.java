package com.familring.notificationservice.model.dto.response;

import com.familring.notificationservice.model.dto.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private Long notificationId;
    private Long receiverUserId;
    private Long senderUserId;
    private String destinationId;
    private NotificationType notificationType;
    private String notificationTitle;
    private String notificationMessage;
    private boolean notificationIsRead;
}
