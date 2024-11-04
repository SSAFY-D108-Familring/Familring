package com.familring.calendarservice.exception.schedule;

import com.familring.calendarservice.exception.base.ApplicationException;
import com.familring.calendarservice.exception.constant.ErrorDetail;

public class ScheduleNotFoundException extends ApplicationException {
    public ScheduleNotFoundException() {
        super(ErrorDetail.SCHEDULE_NOT_FOUND);
    }
}
