package com.familring.familyservice.service.chat.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {
    private final Long roomId;
    private final String message;
    private final String type;

    public NotificationEvent(Object source, Long roomId, String message, String type) {
        super(source);
        this.roomId = roomId;
        this.message = message;
        this.type = type;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getMessage() {
        return message;
    }
}