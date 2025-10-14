package com.rouby.notification.notificationEvent.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum NotificationEventErrorCode implements ErrorCode {
  NOT_EXISTS_USER_DEVICE_INFO(
      "사용자의 알림 전송 가능한 디바이스 정보가 존재하지 않습니다.",
      "NOT_EXISTS_USER_DEVICE_INFO",
      HttpStatus.NOT_FOUND),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
