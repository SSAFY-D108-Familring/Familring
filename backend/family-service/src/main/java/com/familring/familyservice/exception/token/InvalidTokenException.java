package com.familring.familyservice.exception.token;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorCode;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

}
