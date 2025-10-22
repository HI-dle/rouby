package com.rouby.notification.notificationEvent.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotificationEventInfraException extends CustomException {

  private NotificationEventInfraException(ErrorCode errorCode) {
    super(errorCode);
  }

  private NotificationEventInfraException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private NotificationEventInfraException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static NotificationEventInfraException from(ErrorCode errorCode) {
    return new NotificationEventInfraException(errorCode);
  }

  public static NotificationEventInfraException of(HttpStatus httpStatus, String message) {
    return new NotificationEventInfraException(httpStatus, message);
  }

  public static NotificationEventInfraException of(HttpStatus httpStatus, String code, String message) {
    return new NotificationEventInfraException(httpStatus, code, message);
  }
}
