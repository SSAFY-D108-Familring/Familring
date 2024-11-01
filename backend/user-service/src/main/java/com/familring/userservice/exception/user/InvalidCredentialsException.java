package com.familring.userservice.exception.user;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException() {
        super(ErrorDetail.INVALID_CREDENTIALS);
    }

}
