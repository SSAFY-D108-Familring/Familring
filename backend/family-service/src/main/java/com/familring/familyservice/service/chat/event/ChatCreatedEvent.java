package com.familring.familyservice.service.chat.event;

public record ChatCreatedEvent(Long roomId, String content, String sendTime) {
}
