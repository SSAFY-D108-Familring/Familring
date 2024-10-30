package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorCode;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

}
