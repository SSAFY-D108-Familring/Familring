package com.familring.familyservice.model.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_room_entry")
public class ChatRoomEntry {

    @Id
    private String id; // MongoDB Document ID

    private String familyId; // 채팅방 ID
    private String userId; // 사용자 ID

    private LocalDateTime entryTime; // 입장 시간

}