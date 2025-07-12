package com.rouby.user.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  INVALID_USER("잘못된 사용자 입니다.", "INVALID_USER", HttpStatus.UNAUTHORIZED),
  INVALID_USER_PASSWORD("잘못된 사용자 입니다", "INVALID_USER", HttpStatus.UNAUTHORIZED),
  DUPLICATE_EMAIL("이미 사용중인 이메일 입니다.", "DUPLICATE_EMAIL",HttpStatus.CONFLICT),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
