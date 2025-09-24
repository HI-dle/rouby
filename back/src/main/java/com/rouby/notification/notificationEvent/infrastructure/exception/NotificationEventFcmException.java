package com.rouby.notification.notificationEvent.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotificationEventFcmException extends CustomException {

  public NotificationEventFcmException(HttpStatus status, String msg) {
    super(status, msg);
  }
  public HttpStatus status() {
    return super.getStatus();
  }
}