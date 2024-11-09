package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class InterestMissionEndDateNotFoundException extends ApplicationException {
    public InterestMissionEndDateNotFoundException() {
        super(ErrorDetail.NOT_FOUND_INTEREST_MISSION_END_DATE);
    }
}
