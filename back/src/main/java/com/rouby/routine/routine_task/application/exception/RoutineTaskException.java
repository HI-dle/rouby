package com.rouby.routine.routine_task.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class RoutineTaskException extends CustomException {

  private RoutineTaskException(ErrorCode errorCode) {
    super(errorCode);
  }

  private RoutineTaskException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private RoutineTaskException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static RoutineTaskException from(ErrorCode errorCode) {
    return new RoutineTaskException(errorCode);
  }

  public static RoutineTaskException of(HttpStatus httpStatus, String message) {
    return new RoutineTaskException(httpStatus, message);
  }

  public static RoutineTaskException of(HttpStatus httpStatus, String code, String message) {
    return new RoutineTaskException(httpStatus, code, message);
  }
}
