package com.familring.userservice.exception.file;


import com.familring.userservice.exception.base.ApplicationException;
import com.familring.userservice.exception.constant.ErrorDetail;

public class NoContentVoiceException extends ApplicationException {
    public NoContentVoiceException() {
        super(ErrorDetail.NO_CONTENT_VOICE);
    }
}
