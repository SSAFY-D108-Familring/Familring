package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException() {
        super(ErrorDetail.INVALID_TOKEN);
    }

}
