package com.rouby.routine.routine_task.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class RoutineTaskException extends CustomException {

  private RoutineTaskException(ErrorCode errorCode) {
    super(errorCode);
  }
}
