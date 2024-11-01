package com.familring.userservice.exception.base;

import com.familring.userservice.exception.constant.ErrorDetail;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected ApplicationException(ErrorDetail errorDetail) {
        super(errorDetail.getErrorMessage());
        this.httpStatus = errorDetail.getHttpStatus();
    }

}