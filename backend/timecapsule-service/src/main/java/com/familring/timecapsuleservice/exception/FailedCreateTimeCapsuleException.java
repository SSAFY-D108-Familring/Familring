package com.familring.timecapsuleservice.exception;

import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class FailedCreateTimeCapsuleException extends ApplicationException {
    public FailedCreateTimeCapsuleException() {
        super(ErrorDetail.FAILED_CREATE_TIME_CAPSULE);
    }
}
