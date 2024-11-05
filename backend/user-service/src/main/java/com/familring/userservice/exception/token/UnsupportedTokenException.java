package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class UnsupportedTokenException extends ApplicationException {
    public UnsupportedTokenException() {
        super(ErrorDetail.UNSUPPORTED_TOKEN);
    }
}
