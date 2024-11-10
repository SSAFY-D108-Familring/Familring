package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class AlreadyExistSelectInterestAnswerException extends ApplicationException {
    public AlreadyExistSelectInterestAnswerException() {
        super(ErrorDetail.EXIST_SELECT_QUESTION_ANSWER);
    }
}
