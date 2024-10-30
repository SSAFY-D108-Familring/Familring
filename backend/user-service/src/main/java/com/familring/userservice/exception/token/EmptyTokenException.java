package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorCode;

public class EmptyTokenException extends ApplicationException {
    public EmptyTokenException() {
        super(ErrorCode.EMPTY_TOKEN);
    }
}
