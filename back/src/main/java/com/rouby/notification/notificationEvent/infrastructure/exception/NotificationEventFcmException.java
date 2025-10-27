package com.rouby.notification.notificationEvent.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;


public class NotificationEventFcmException extends CustomException {

  private NotificationEventFcmException(ErrorCode errorCode) {
    super(errorCode);
  }

  private NotificationEventFcmException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private NotificationEventFcmException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static NotificationEventFcmException from(ErrorCode errorCode) {
    return new NotificationEventFcmException(errorCode);
  }

  public static NotificationEventFcmException of(HttpStatus httpStatus, String message) {
    return new NotificationEventFcmException(httpStatus, message);
  }

  public static NotificationEventFcmException of(HttpStatus httpStatus, String code, String message) {
    return new NotificationEventFcmException(httpStatus, code, message);
  }
}