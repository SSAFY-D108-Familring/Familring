package com.familring.familyservice.exception.family;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class FamilyNotFoundException extends ApplicationException {
    public FamilyNotFoundException() {
        super(ErrorDetail.NOT_FOUND_FAMILY);
    }
}
