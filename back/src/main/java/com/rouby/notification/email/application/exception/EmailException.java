package com.rouby.notification.email.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class EmailException extends CustomException {

  private EmailException(ErrorCode errorCode) {
    super(errorCode);
  }

  private EmailException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private EmailException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static EmailException from(ErrorCode errorCode) {
    return new EmailException(errorCode);
  }

  public static EmailException of(HttpStatus httpStatus, String message) {
    return new EmailException(httpStatus, message);
  }

  public static EmailException of(HttpStatus httpStatus, String code, String message) {
    return new EmailException(httpStatus, code, message);
  }
}
