package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class InvalidRefreshTokenException extends ApplicationException {
    public InvalidRefreshTokenException() {
        super(ErrorDetail.EXPIRED_TOKEN);
    }
}
