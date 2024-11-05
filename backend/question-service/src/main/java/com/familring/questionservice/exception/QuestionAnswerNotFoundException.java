package com.familring.questionservice.exception;

import com.familring.questionservice.exception.base.ApplicationException;
import com.familring.questionservice.exception.constant.ErrorDetail;

public class QuestionAnswerNotFoundException extends ApplicationException {
    public QuestionAnswerNotFoundException() {
        super(ErrorDetail.NOT_FOUND_QUESTION_ANSWER);
    }
}
