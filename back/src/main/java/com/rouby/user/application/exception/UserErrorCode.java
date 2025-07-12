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
  DUPLICATE_EMAIL("이미 사용중인 이메일 입니다.", "DUPLICATE_EMAIL", HttpStatus.CONFLICT),
  INVALID_EMAIL_VERIFICATION("인증번호가 올바르지 않거나 유효시간(5분)이 경과되었습니다. 다시 확인해주세요.",
      "INVALID_EMAIL_VERIFICATION", HttpStatus.UNAUTHORIZED),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
