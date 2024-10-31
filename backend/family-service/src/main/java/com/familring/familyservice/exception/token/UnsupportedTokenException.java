package com.familring.familyservice.exception.token;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorCode;

public class UnsupportedTokenException extends ApplicationException {
    public UnsupportedTokenException() {
        super(ErrorCode.UNSUPPORTED_TOKEN);
    }
}
