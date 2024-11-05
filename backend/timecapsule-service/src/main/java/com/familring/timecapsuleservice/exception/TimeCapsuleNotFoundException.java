package com.familring.timecapsuleservice.exception;

import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class TimeCapsuleNotFoundException extends ApplicationException {
    public TimeCapsuleNotFoundException() {
        super(ErrorDetail.NOT_FOUND_TIME_CAPSULE);
    }
}
