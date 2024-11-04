package com.familring.familyservice.exception.family;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class AlreadyInFamilyException extends ApplicationException {
    public AlreadyInFamilyException() {
        super(ErrorDetail.ALREADY_IN_FAMILY);
    }
}
