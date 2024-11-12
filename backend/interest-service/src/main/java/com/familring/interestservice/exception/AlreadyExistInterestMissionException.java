package com.familring.interestservice.exception;

import com.familring.interestservice.exception.base.ApplicationException;
import com.familring.interestservice.exception.constant.ErrorDetail;

public class AlreadyExistInterestMissionException extends ApplicationException {

    public AlreadyExistInterestMissionException() {
        super(ErrorDetail.EXIST_INTEREST_MISSION);
    }
}
