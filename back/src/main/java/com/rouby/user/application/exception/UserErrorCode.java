package com.rouby.user.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  DUPLICATE_EMAIL("이미 사용중인 이메일 입니다.", "DUPLICATE_EMAIL",HttpStatus.CONFLICT),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
