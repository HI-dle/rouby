package com.rouby.notification.notificationEvent.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotificationEventFcmRetryableException extends CustomException {

  private final String retryAfter;

  public NotificationEventFcmRetryableException(HttpStatus status, String msg, String retryAfter) {
    super(status, msg);
    this.retryAfter = retryAfter;
  }
  public HttpStatus status() {
    return super.getStatus();
  }
}