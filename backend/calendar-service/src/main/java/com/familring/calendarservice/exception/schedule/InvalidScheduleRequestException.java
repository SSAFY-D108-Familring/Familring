package com.familring.calendarservice.exception.schedule;

import com.familring.calendarservice.exception.base.ApplicationException;
import com.familring.calendarservice.exception.constant.ErrorDetail;

public class InvalidScheduleRequestException extends ApplicationException {
    public InvalidScheduleRequestException() {
        super(ErrorDetail.INVALID_SCHEDULE_REQUEST);
    }
}
