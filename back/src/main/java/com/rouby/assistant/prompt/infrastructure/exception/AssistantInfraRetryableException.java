package com.rouby.assistant.prompt.infrastructure.exception;

import com.rouby.common.exception.RateLimitException;
import org.springframework.http.HttpStatus;

public class AssistantInfraRetryableException extends RateLimitException {

  private AssistantInfraRetryableException(HttpStatus status, String msg, long retryAfterSeconds) {
    super(status, msg, retryAfterSeconds);
  }

  public static AssistantInfraRetryableException of(HttpStatus status, String msg, long retryAfterSeconds) {
    return new AssistantInfraRetryableException(status, msg, retryAfterSeconds);
  }
}