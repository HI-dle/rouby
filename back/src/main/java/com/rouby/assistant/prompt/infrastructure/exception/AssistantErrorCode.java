package com.rouby.assistant.prompt.infrastructure.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AssistantErrorCode implements ErrorCode {

  SERVICE_UNAVAILABLE("AI ASSISTANT가 가용하지 않습니다.", "ASSISTANT_SERVICE_UNAVAILABLE",
      HttpStatus.SERVICE_UNAVAILABLE),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}