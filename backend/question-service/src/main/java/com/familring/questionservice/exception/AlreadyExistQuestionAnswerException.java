package com.familring.questionservice.exception;


import com.familring.questionservice.exception.base.ApplicationException;
import com.familring.questionservice.exception.constant.ErrorDetail;

public class AlreadyExistQuestionAnswerException extends ApplicationException {
    public AlreadyExistQuestionAnswerException() {
        super(ErrorDetail.EXIST_QUESTION_ANSWER);
    }
}
