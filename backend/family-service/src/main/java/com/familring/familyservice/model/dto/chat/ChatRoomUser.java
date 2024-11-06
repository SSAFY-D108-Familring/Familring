package com.familring.familyservice.model.dto.chat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_user_id")
    private Long id;

//    @Column(length = 50, nullable = false)
//    @Size(max = 50)
//    private String chatRoomTitle;
//    @Column(length = 20)
//    @Size(max = 20)
//    private String childName;

    @NotNull
    private Long userId;

//    @Column(length = 1)
//    @Size(min = 1, max = 1)
//    private String opponentUserType;
    private boolean isAlarm;
    private int unreadCount;
    @Column(length = 50)
    @Size(max = 50)
    private String lastContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    private ChatRoomUser(Long id, String chatRoomTitle, String childName, Long userId, String opponentUserType, boolean isAlarm, int unreadCount, String lastContent, ChatRoom chatRoom) {
        this.id = id;
        this.chatRoomTitle = chatRoomTitle;
        this.childName = childName;
        this.userId = userId;
        this.opponentUserType = opponentUserType;
        this.isAlarm = isAlarm;
        this.unreadCount = unreadCount;
        this.lastContent = lastContent;
        this.chatRoom = chatRoom;
    }

    public void updateUpdateChat(String lastContent) {
        if (lastContent.length() >= 50) {
            lastContent = lastContent.substring(0, 49);
        }
        this.lastContent = lastContent;
        this.unreadCount++;
    }

    public void read() {
        this.unreadCount = 0;
    }
}
