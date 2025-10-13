package com.rouby.assistant.feedback.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class FeedbackException extends CustomException {

  private FeedbackException(ErrorCode errorCode) {
    super(errorCode);
  }
}
