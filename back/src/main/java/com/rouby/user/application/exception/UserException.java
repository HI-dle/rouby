package com.rouby.user.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class UserException extends CustomException {

  private UserException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static UserException of(ErrorCode errorCode) {
    return new UserException(errorCode);
  }
}
