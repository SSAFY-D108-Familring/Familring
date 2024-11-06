package com.familring.familyservice.exception.chat;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class ChatRoomNotFoundException extends ApplicationException {
    public ChatRoomNotFoundException() {
        super(ErrorDetail.NOT_FOUND_CHAT_ROOM);
    }
}
