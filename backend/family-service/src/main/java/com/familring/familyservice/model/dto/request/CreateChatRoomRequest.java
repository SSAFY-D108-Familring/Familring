package com.familring.familyservice.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateChatRoomRequest {

    private Long userId;
//    private String opponentUserKey;
//    private String opponentUserName;
//    private String opponentUserType;
//    private Long schoolClassId;
//    private int loginUserType;

    @Builder
    private CreateChatRoomRequest(Long userId) {
        this.userId = userId;
    }

//    @Builder
//    private CreateChatRoomRequest(String loginUserToken, String opponentUserKey, String opponentUserName, String opponentUserType, Long schoolClassId, int loginUserType) {
//        this.loginUserToken = loginUserToken;
//        this.opponentUserKey = opponentUserKey;
//        this.opponentUserName = opponentUserName;
//        this.opponentUserType = opponentUserType;
//        this.schoolClassId = schoolClassId;
//        this.loginUserType = loginUserType;
//    }

}
