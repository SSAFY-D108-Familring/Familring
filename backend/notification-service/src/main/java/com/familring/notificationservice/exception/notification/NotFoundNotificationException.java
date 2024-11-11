package com.familring.notificationservice.exception.notification;

import com.familring.notificationservice.exception.base.ApplicationException;
import com.familring.notificationservice.exception.constant.ErrorDetail;

public class NotFoundNotificationException extends ApplicationException {
    public NotFoundNotificationException() {
        super(ErrorDetail.NOT_FOUND_NOTIFICATION);
    }
}