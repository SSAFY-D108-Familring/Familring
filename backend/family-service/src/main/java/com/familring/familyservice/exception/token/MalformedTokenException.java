package com.familring.familyservice.exception.token;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorCode;

public class MalformedTokenException extends ApplicationException {
    public MalformedTokenException() {
        super(ErrorCode.MALFORMED_TOKEN);
    }
}
