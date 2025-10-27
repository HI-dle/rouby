package com.rouby.assistant.feedback.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class FeedbackException extends CustomException {

  private FeedbackException(ErrorCode errorCode) {
    super(errorCode);
  }

  private FeedbackException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private FeedbackException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static FeedbackException from(ErrorCode errorCode) {
    return new FeedbackException(errorCode);
  }

  public static FeedbackException of(HttpStatus httpStatus, String message) {
    return new FeedbackException(httpStatus, message);
  }

  public static FeedbackException of(HttpStatus httpStatus, String code, String message) {
    return new FeedbackException(httpStatus, code, message);
  }
}
