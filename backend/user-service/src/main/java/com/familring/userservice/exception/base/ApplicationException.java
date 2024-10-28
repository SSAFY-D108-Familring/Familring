package com.familring.userservice.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final HttpStatus httpStatus;

    protected ApplicationException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}