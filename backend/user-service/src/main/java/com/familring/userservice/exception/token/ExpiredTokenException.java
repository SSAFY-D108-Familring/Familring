package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class ExpiredTokenException extends ApplicationException {
    public ExpiredTokenException() {
        super(ErrorDetail.EXPIRED_TOKEN);
    }
}
