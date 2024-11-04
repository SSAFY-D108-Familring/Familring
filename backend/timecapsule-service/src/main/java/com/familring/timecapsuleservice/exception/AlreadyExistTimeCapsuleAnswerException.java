package com.familring.timecapsuleservice.exception;

import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class AlreadyExistTimeCapsuleAnswerException extends ApplicationException {
    public AlreadyExistTimeCapsuleAnswerException() {
        super(ErrorDetail.EXIST_TIME_CAPSULE_ANSWER);
    }
}
