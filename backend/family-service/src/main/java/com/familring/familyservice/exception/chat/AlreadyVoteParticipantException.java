package com.familring.familyservice.exception.chat;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class AlreadyVoteParticipantException extends ApplicationException {
    public AlreadyVoteParticipantException() {
        super(ErrorDetail.ALREADY_PARTICIPATED);
    }
}