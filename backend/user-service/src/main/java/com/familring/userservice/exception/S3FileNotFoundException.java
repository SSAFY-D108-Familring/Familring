package com.familring.userservice.exception;

import com.familring.userservice.exception.base.ApplicationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class S3FileNotFoundException extends ApplicationException {
    public S3FileNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}