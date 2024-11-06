package com.familring.familyservice.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateChatRoomResponse {

    private Long roomId;
//    private String opponentUserName;
//    private char opponentUserType;
//    private String opponentUsersChildName;

    @Builder
    private CreateChatRoomResponse(Long roomId) {
        this.roomId = roomId;
    }

//    @Builder
//    private CreateChatRoomResponse(Long roomId, String opponentUserName, char opponentUserType, String opponentUsersChildName) {
//        this.roomId = roomId;
//        this.opponentUserName = opponentUserName;
//        this.opponentUserType = opponentUserType;
//        this.opponentUsersChildName = opponentUsersChildName;
//    }
}
