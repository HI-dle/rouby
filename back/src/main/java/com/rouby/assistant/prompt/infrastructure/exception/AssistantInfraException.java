package com.rouby.assistant.prompt.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class AssistantInfraException extends CustomException {

  private AssistantInfraException(ErrorCode errorCode) {
      super(errorCode);
  }

  private AssistantInfraException(HttpStatus status, String message) {
    super(status, message);
  }

  private AssistantInfraException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static AssistantInfraException from(ErrorCode errorCode) {
    return new AssistantInfraException(errorCode);
  }

  public static AssistantInfraException of(HttpStatus httpStatus, String message) {
    return new AssistantInfraException(httpStatus, message);
  }

  public static AssistantInfraException of(HttpStatus httpStatus, String code, String message) {
    return new AssistantInfraException(httpStatus, code, message);
  }
}

