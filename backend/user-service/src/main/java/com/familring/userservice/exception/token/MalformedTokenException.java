package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorCode;

public class MalformedTokenException extends ApplicationException {
    public MalformedTokenException() {
        super(ErrorCode.MALFORMED_TOKEN);
    }
}
