package com.rouby.notification.notificationEvent.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotificationEventException extends CustomException {

  private NotificationEventException(ErrorCode errorCode) {
    super(errorCode);
  }

  private NotificationEventException(HttpStatus status, String message) {
    super(status, message);
  }

  private NotificationEventException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static NotificationEventException from(ErrorCode errorCode) {
    return new NotificationEventException(errorCode);
  }

  public static NotificationEventException of(HttpStatus httpStatus, String message) {
    return new NotificationEventException(httpStatus, message);
  }

  public static NotificationEventException of(HttpStatus httpStatus, String code, String message) {
    return new NotificationEventException(httpStatus, code, message);
  }
}
