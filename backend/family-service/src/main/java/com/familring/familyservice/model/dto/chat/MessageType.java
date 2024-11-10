package com.familring.familyservice.model.dto.chat;

public enum MessageType {
    MESSAGE,        // 일반 메세지
    VOICE,          // 음성 메세지
    VOTE,           // 투표
    VOTE_RESPONSE,  // 투표 응답
    VOTE_RESULT     // 투표 결과
}
