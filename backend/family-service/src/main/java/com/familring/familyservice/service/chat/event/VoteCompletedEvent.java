package com.familring.familyservice.service.chat.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class VoteCompletedEvent {
    private final Long roomId;
    private final String voteId;
    private final Long senderId;
    private final String voteTitle;
    private final Map<String, Integer> voteResult;
    private final LocalDateTime createdAt;
}
