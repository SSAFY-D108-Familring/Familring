package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class InterestNotFoundException extends ApplicationException {
    public InterestNotFoundException() {
        super(ErrorDetail.NOT_FOUND_INTEREST);
    }
}
