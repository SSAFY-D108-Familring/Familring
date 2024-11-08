package com.familring.familyservice.exception.chat;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class VoteNotFoundException extends ApplicationException {
    public VoteNotFoundException() {
        super(ErrorDetail.NOT_FOUND_VOTE);
    }
}