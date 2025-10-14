package com.rouby.notification.notificationEvent.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class NotificationEventException extends CustomException {

  private NotificationEventException(ErrorCode errorCode) {
    super(errorCode);
  }
}
