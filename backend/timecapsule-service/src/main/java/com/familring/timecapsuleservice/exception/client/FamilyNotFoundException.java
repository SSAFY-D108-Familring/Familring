package com.familring.timecapsuleservice.exception.client;


import com.familring.timecapsuleservice.exception.base.ApplicationException;
import com.familring.timecapsuleservice.exception.constant.ErrorDetail;

public class FamilyNotFoundException extends ApplicationException {
    public FamilyNotFoundException() {
        super(ErrorDetail.NOT_FOUND_FAMILY);
    }
}
