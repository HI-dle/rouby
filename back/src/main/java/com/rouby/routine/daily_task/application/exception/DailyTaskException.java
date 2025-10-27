package com.rouby.routine.daily_task.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class DailyTaskException extends CustomException {

  private DailyTaskException(ErrorCode errorCode) {
    super(errorCode);
  }

  private DailyTaskException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private DailyTaskException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static DailyTaskException from(ErrorCode errorCode) {
    return new DailyTaskException(errorCode);
  }

  public static DailyTaskException of(HttpStatus httpStatus, String message) {
    return new DailyTaskException(httpStatus, message);
  }

  public static DailyTaskException of(HttpStatus httpStatus, String code, String message) {
    return new DailyTaskException(httpStatus, code, message);
  }
}
