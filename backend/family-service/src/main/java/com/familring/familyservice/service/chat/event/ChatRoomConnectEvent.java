package com.familring.familyservice.service.chat.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomConnectEvent {
    private Long roomId;
    private Long userId;
}
