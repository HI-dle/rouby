package com.rouby.notification.notificationEvent.infrastructure.exception;

import com.rouby.common.exception.RateLimitException;
import org.springframework.http.HttpStatus;

public class NotificationEventFcmRetryableException extends RateLimitException {

  public NotificationEventFcmRetryableException(
      HttpStatus status, String message, long retryAfterSeconds) {
    super(status, message, retryAfterSeconds);
  }

  public static NotificationEventFcmRetryableException of(
      HttpStatus httpStatus, String message, long retryAfterSeconds) {
    return new NotificationEventFcmRetryableException(httpStatus, message, retryAfterSeconds);
  }
}