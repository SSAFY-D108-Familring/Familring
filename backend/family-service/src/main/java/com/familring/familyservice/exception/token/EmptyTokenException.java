package com.familring.familyservice.exception.token;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorCode;

public class EmptyTokenException extends ApplicationException {
    public EmptyTokenException() {
        super(ErrorCode.EMPTY_TOKEN);
    }
}
