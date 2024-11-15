package com.familring.timecapsuleservice.exception;

import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class MinimumDurationTimeCapsuleException extends ApplicationException {

    public MinimumDurationTimeCapsuleException() {
        super(ErrorDetail.TIME_CAPSULE_MINIMUM_DURATION);
    }
}
