package com.familring.userservice.exception;

import com.familring.userservice.exception.base.ApplicationException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
