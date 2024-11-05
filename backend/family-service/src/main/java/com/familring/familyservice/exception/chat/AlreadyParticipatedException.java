package com.familring.familyservice.exception.chat;

import com.familring.familyservice.exception.base.ApplicationException;
import com.familring.familyservice.exception.constant.ErrorDetail;

public class AlreadyParticipatedException extends ApplicationException {
  public AlreadyParticipatedException() {
    super(ErrorDetail.ALREADY_PARTICIPATED);
  }
}

