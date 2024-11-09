package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class InterestAnswerUserNotFoundException extends ApplicationException {
    public InterestAnswerUserNotFoundException() {
        super(ErrorDetail.NOT_FOUND_INTEREST_ANSWER_USER);
    }
}
