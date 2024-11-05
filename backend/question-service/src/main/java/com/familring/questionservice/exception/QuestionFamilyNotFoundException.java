package com.familring.questionservice.exception;

import com.familring.questionservice.exception.base.ApplicationException;
import com.familring.questionservice.exception.constant.ErrorDetail;

public class QuestionFamilyNotFoundException extends ApplicationException {
    public QuestionFamilyNotFoundException() {
        super(ErrorDetail.NOT_FOUND_QUESTION_FAMILY);
    }
}
