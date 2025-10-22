package com.rouby.assistant.prompt.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class PromptException extends CustomException {

  private PromptException(ErrorCode errorCode) {
    super(errorCode);
  }

  private PromptException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private PromptException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static PromptException from(ErrorCode errorCode) {
    return new PromptException(errorCode);
  }

  public static PromptException of(HttpStatus httpStatus, String message) {
    return new PromptException(httpStatus, message);
  }

  public static PromptException of(HttpStatus httpStatus, String code, String message) {
    return new PromptException(httpStatus, code, message);
  }
}
