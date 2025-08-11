package com.rouby.user.user.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class UserException extends CustomException {

  private UserException(ErrorCode errorCode) {
    super(errorCode);
  }
}
