package com.rouby.notification.notificationtemplate.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;

public class NotificationTemplateException extends CustomException {

  private NotificationTemplateException(ErrorCode errorCode) {
    super(errorCode);
  }
}
