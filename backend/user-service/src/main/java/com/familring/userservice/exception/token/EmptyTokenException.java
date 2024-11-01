package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class EmptyTokenException extends ApplicationException {
    public EmptyTokenException() {
        super(ErrorDetail.EMPTY_TOKEN);
    }
}
