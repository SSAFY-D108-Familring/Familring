package com.familring.familyservice.service.chat;

import com.familring.familyservice.service.chat.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {

    private final ApplicationEventPublisher eventPublisher;

    public void notifyReadStatusUpdate(Long roomId) {
        eventPublisher.publishEvent(new NotificationEvent(this, roomId, "UPDATE_READ_STATUS", "read"));
    }

    public void notifyRoomExit(Long roomId, Long userId) {
        eventPublisher.publishEvent(new NotificationEvent(this, roomId, userId + "님이 채팅방을 나갔습니다.", "read"));
    }

    public void notifyVoteConflict(Long roomId, Long userId) {
        eventPublisher.publishEvent(new NotificationEvent(this, roomId, "투표를 중복 참여하셨습니다.", "error"));
    }
}
