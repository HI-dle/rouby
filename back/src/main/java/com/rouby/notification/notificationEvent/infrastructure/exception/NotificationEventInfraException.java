package com.rouby.notification.notificationEvent.infrastructure.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class NotificationEventInfraException extends CustomException {

  private NotificationEventInfraException(ErrorCode errorCode) {
    super(errorCode);
  }
}
