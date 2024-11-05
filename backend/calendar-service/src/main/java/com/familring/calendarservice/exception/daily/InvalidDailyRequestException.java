package com.familring.calendarservice.exception.daily;

import com.familring.calendarservice.exception.base.ApplicationException;
import com.familring.calendarservice.exception.constant.ErrorDetail;

public class InvalidDailyRequestException extends ApplicationException {
    public InvalidDailyRequestException() {
        super(ErrorDetail.INVALID_DAILY_REQUEST);
    }
}
