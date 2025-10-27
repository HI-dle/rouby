package com.rouby.user.user.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class UserException extends CustomException {

  private UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  private UserException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private UserException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static UserException from(ErrorCode errorCode) {
    return new UserException(errorCode);
  }

  public static UserException of(HttpStatus httpStatus, String message) {
    return new UserException(httpStatus, message);
  }

  public static UserException of(HttpStatus httpStatus, String code, String message) {
    return new UserException(httpStatus, code, message);
  }
}
