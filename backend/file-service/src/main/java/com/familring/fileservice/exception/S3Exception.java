package com.familring.fileservice.exception;

import com.familring.fileservice.exception.base.ApplicationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class S3Exception extends ApplicationException {
    public S3Exception(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

}