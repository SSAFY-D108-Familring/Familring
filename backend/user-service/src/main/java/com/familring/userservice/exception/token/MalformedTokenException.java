package com.familring.userservice.exception.token;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class MalformedTokenException extends ApplicationException {
    public MalformedTokenException() {
        super(ErrorDetail.MALFORMED_TOKEN);
    }
}
