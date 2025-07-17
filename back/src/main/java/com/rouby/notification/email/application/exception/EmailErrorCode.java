package com.rouby.notification.email.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {
  EMAIL_SEND_FAILED("이메일 전송에 실패했습니다.", "EMAIL_SEND_FAILED", HttpStatus.SERVICE_UNAVAILABLE),
  EMAIL_LOG_SAVE_FAILED("이메일 로그 저장에 실패했습니다.", "EMAIL_LOG_SAVE_FAILED", HttpStatus.INTERNAL_SERVER_ERROR),
  EMAIL_LIMIT_EXCEEDED("이메일 전송 횟수를 초과했습니다.", "EMAIL_LIMIT_EXCEEDED", HttpStatus.TOO_MANY_REQUESTS),

  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
