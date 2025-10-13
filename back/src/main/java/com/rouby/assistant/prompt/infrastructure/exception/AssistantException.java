package com.rouby.assistant.prompt.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class AssistantException extends CustomException {

  private AssistantException(ErrorCode errorCode) {
      super(errorCode);
  }
}

