package com.familring.familyservice.model.dto.chat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id", nullable = false, updatable = false)
    private Long id;

    @Builder
    private ChatRoom(Long id) {
        this.id = id;
    }
}
