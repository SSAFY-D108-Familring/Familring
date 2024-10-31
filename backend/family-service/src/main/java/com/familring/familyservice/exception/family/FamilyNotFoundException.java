package com.familring.familyservice.exception.family;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorCode;

public class FamilyNotFoundException extends ApplicationException {
    public FamilyNotFoundException() {
        super(ErrorCode.NOT_FOUND_FAMILY);
    }
}
