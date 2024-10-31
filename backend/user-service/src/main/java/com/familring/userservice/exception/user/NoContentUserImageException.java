package com.familring.userservice.exception.user;

import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorCode;

public class NoContentUserImageException extends ApplicationException {
    public NoContentUserImageException() {
        super(ErrorCode.NO_CONTENT_USER_IMAGE);
    }

}
