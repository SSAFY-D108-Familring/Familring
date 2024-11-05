package com.familring.familyservice.exception.family;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class AlreadyFamilyRoleException extends ApplicationException {
    public AlreadyFamilyRoleException() {
        super(ErrorDetail.ALREADY_ROLE_IN_FAMILY);
    }
}
