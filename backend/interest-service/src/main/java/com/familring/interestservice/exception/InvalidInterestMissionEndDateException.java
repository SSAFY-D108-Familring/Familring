package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class InvalidInterestMissionEndDateException extends ApplicationException {
    public InvalidInterestMissionEndDateException() {
        super(ErrorDetail.INVALID_INTEREST_MISSION_END_DATE);
    }
}
