package com.familring.familyservice.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private Long senderId; // 발신자 id
    private String content; // 채팅 내용
}
