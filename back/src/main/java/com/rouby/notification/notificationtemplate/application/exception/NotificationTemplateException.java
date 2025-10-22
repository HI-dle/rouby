package com.rouby.notification.notificationtemplate.application.exception;

import com.rouby.common.exception.CustomException;
import com.rouby.common.exception.type.ErrorCode;
import org.springframework.http.HttpStatus;

public class NotificationTemplateException extends CustomException {

  private NotificationTemplateException(ErrorCode errorCode) {
    super(errorCode);
  }

  private NotificationTemplateException(HttpStatus httpStatus, String message) {
    super(httpStatus, message);
  }

  private NotificationTemplateException(HttpStatus httpStatus, String code, String message) {
    super(httpStatus, code, message);
  }

  public static NotificationTemplateException from(ErrorCode errorCode) {
    return new NotificationTemplateException(errorCode);
  }

  public static NotificationTemplateException of(HttpStatus httpStatus, String message) {
    return new NotificationTemplateException(httpStatus, message);
  }

  public static NotificationTemplateException of(HttpStatus httpStatus, String code, String message) {
    return new NotificationTemplateException(httpStatus, code, message);
  }
}
