package com.rouby.notification.notificationtemplate.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotificationTemplateErrorCode implements ErrorCode {
  NOTIFICATION_TEMPLATE_NOT_FOUND("알림 템플릿을 찾을 수 없습니다.", "NOTIFICATION_TEMPLATE_NOT_FOUND",
      HttpStatus.NOT_FOUND),
  NOTIFICATION_TEMPLATE_ALREADY_EXISTS("템플릿이 이미 존재합니다.", "NOTIFICATION_TEMPLATE_ALREADY_EXISTS",
      HttpStatus.NOT_FOUND);

  private final String message;
  private final String code;
  private final HttpStatus status;
}
