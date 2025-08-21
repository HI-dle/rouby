package com.rouby.assistant.prompt.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class PromptException extends CustomException {

  private PromptException(ErrorCode errorCode) {
    super(errorCode);
  }
}
