package com.familring.questionservice.exception;

import com.familring.questionservice.exception.constant.ErrorDetail;
import com.familring.questionservice.exception.base.ApplicationException;

public class QuestionNotFoundException extends ApplicationException {
    public QuestionNotFoundException() {
        super(ErrorDetail.NOT_FOUND_QUESTION);
    }
}
