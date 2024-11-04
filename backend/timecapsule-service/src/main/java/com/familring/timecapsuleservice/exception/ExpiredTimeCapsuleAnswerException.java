package com.familring.timecapsuleservice.exception;

import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class ExpiredTimeCapsuleAnswerException extends ApplicationException {
    public ExpiredTimeCapsuleAnswerException() {
        super(ErrorDetail.EXPIRED_TIME_CAPSULE_ANSWER);
    }
}
