package com.familring.familyservice.exception.base;

import com.familring.familyservice.exception.constant.ErrorDetail;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    protected ApplicationException(ErrorDetail errorDetail) {
        super(errorDetail.getErrorMessage());
        this.errorCode = errorDetail.getErrorCode();
        this.httpStatus = errorDetail.getHttpStatus();
    }

}