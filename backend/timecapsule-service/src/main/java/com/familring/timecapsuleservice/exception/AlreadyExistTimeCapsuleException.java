package com.familring.timecapsuleservice.exception;

import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class AlreadyExistTimeCapsuleException extends ApplicationException {

    public AlreadyExistTimeCapsuleException() {
        super(ErrorDetail.EXIST_TIME_CAPSULE);
    }
}