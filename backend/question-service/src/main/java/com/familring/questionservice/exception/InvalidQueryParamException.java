package com.familring.questionservice.exception;

import com.familring.questionservice.exception.base.ApplicationException;
import com.familring.questionservice.exception.constant.ErrorDetail;

public class InvalidQueryParamException extends ApplicationException {
    public InvalidQueryParamException() {
        super(ErrorDetail.INVALID_QUERY_PARAM);
    }
}
