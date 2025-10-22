package com.rouby.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class RateLimitException extends CustomException {

  private final long retryAfterSeconds;

  protected RateLimitException(HttpStatus httpStatus, String message, long retryAfterSeconds) {

    super(httpStatus, "RETRYABLE_EXCEPTION", message);
    this.retryAfterSeconds = retryAfterSeconds;
  }
}
