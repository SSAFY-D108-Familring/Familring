package com.familring.userservice.exception;

import com.familring.userservice.exception.base.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserException extends ApplicationException {
    public UserException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}
