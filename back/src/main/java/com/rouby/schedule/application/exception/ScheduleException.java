package com.rouby.schedule.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class ScheduleException extends CustomException {

  private ScheduleException(ErrorCode errorCode) {
    super(errorCode);
  }

  private ScheduleException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  public static ScheduleException of(ErrorCode errorCode, String message) {
    return new ScheduleException(errorCode, message);
  }
}
