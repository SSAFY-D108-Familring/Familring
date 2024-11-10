package com.familring.familyservice.service.chat.event;

import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {
    private final Long roomId;
    private final String message;

    public NotificationEvent(Object source, Long roomId, String message) {
        super(source);
        this.roomId = roomId;
        this.message = message;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getMessage() {
        return message;
    }
}