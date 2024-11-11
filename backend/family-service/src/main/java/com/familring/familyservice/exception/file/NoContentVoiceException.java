package com.familring.familyservice.exception.file;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class NoContentVoiceException extends ApplicationException {
    public NoContentVoiceException() {
        super(ErrorDetail.NO_CONTENT_VOICE);
    }
}
