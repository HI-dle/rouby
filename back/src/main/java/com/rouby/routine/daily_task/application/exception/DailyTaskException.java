package com.rouby.routine.daily_task.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class DailyTaskException extends CustomException {

  private DailyTaskException(ErrorCode errorCode) {
    super(errorCode);
  }
}
