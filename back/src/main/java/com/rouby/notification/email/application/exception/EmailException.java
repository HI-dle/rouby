package com.rouby.notification.email.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class EmailException extends CustomException {

  private EmailException(ErrorCode errorCode) {
    super(errorCode);
  }
}
