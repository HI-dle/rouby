package com.rouby.schedule.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class ScheduleException extends CustomException {

  private ScheduleException(ErrorCode errorCode) {
    super(errorCode);
  }

  private ScheduleException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }

  private ScheduleException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private ScheduleException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static ScheduleException from(ErrorCode errorCode) {
    return new ScheduleException(errorCode);
  }

  public static ScheduleException of(ErrorCode errorCode, String message) {
    return new ScheduleException(errorCode, message);
  }

  public static ScheduleException of(HttpStatus httpStatus, String message) {
    return new ScheduleException(httpStatus, message);
  }

  public static ScheduleException of(HttpStatus httpStatus, String code, String message) {
    return new ScheduleException(httpStatus, code, message);
  }
}
