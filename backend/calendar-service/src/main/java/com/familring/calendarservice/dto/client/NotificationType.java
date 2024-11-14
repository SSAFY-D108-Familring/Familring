package com.familring.calendarservice.dto.client;

public enum NotificationType {
    KNOCK,              // 랜덤 질문 노크
    MENTION_SCHEDULE,   // 일정 언급
    MENTION_CHAT,       // 채팅 언급
    RANDOM_QUESTION,    // 랜덤 질문 열림
    TIMECAPSULE,        // 타임캡슐 열어보는 시간
    INTEREST_PICK,      // 관심사 뽑힘
    INTEREST_COMPLETE   // 모든 가족 구성원이 관심사 인증 완료
}
