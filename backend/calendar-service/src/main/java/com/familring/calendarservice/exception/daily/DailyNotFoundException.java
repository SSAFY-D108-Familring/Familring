package com.familring.calendarservice.exception.daily;

import com.familring.calendarservice.exception.base.ApplicationException;
import com.familring.calendarservice.exception.constant.ErrorDetail;

public class DailyNotFoundException extends ApplicationException {
    public DailyNotFoundException() {
        super(ErrorDetail.DAILY_NOT_FOUND);
    }
}
