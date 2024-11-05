package com.familring.userservice.exception.user;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class AlreadyUserException extends ApplicationException {
    public AlreadyUserException() {
        super(ErrorDetail.ALREADY_USER);
    }
}
