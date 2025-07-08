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
  INVALID_USER("존재하지 않는 사용자 입니다.", "INVALID_USER", HttpStatus.NOT_FOUND),
  INVALID_USER_PASSWORD("잘못된 비밀번호 입니다.", "INVALID_USER_PASSWORD", HttpStatus.NOT_FOUND),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
