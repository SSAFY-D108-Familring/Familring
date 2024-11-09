package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class AlreadyExistInterestMissionEndDateException extends ApplicationException {
    public AlreadyExistInterestMissionEndDateException() {
        super(ErrorDetail.EXIST_INTEREST_MISSION_END_DATE);
    }
}
