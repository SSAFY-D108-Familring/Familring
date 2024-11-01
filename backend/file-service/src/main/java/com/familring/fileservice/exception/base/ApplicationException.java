package com.familring.fileservice.exception.base;

import com.familring.fileservice.exception.constant.ErrorDetail;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    protected ApplicationException(ErrorDetail errorDetail) {
        super(errorDetail.getErrorMessage());
        this.errorCode = getErrorCode();
        this.httpStatus = errorDetail.getHttpStatus();
    }

}