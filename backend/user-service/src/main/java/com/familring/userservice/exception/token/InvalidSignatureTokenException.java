package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class InvalidSignatureTokenException extends ApplicationException {
    public InvalidSignatureTokenException() {
        super(ErrorDetail.INVALID_SIGNATURE);
    }
}
