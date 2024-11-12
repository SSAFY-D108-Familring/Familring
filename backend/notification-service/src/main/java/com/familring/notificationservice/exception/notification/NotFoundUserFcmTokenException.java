package com.familring.notificationservice.exception.notification;

import com.familring.notificationservice.exception.base.ApplicationException;
import com.familring.notificationservice.exception.constant.ErrorDetail;

public class NotFoundUserFcmTokenException extends ApplicationException {
    public NotFoundUserFcmTokenException() {
        super(ErrorDetail.NOT_FOUND_USER_FCM_TOKEN);
    }
}
