package com.rouby.user.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 08.
 */
@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
  INVALID_USER("잘못된 사용자 입니다.", "INVALID_USER", HttpStatus.UNAUTHORIZED),
  INVALID_USER_PASSWORD("잘못된 사용자 입니다", "INVALID_USER", HttpStatus.UNAUTHORIZED),
  DUPLICATE_EMAIL("이미 사용중인 이메일 입니다.", "DUPLICATE_EMAIL", HttpStatus.CONFLICT),
  INVALID_EMAIL_VERIFICATION("인증번호가 올바르지 않거나 유효시간(5분)이 경과되었습니다. 다시 확인해주세요.",
      "INVALID_EMAIL_VERIFICATION", HttpStatus.UNAUTHORIZED),
  EMAIL_NOT_FOUND("이메일을 찾을 수 없습니다.", "EMAIL_NOT_FOUND", HttpStatus.NOT_FOUND),
  INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", "INVALID_PASSWORD", HttpStatus.UNAUTHORIZED),
  USER_NOT_FOUND("존재하지 않는 유저입니다.", "USER_NOT_FOUND", HttpStatus.NOT_FOUND),
  PASSWORD_TOKEN_EXPIRED("비밀번호 재설정 토큰이 만료되었습니다.", "PASSWORD_TOKEN_EXPIRED",
      HttpStatus.UNAUTHORIZED),
  EMAIL_NOT_VERIFIED("이메일 인증이 필요합니다. 인증 후 이용해주세요.", "EMAIL_NOT_VERIFIED", HttpStatus.UNAUTHORIZED),
  INVALID_EMAIL_VERIFICATION_TOKEN("유효하지 않은 이메일 토큰 입니다.", "INVALID_EMAIL_VERIFICATION_TOKEN",
      HttpStatus.UNAUTHORIZED),
  EMAIL_VERIFICATION_TOKEN_MISMATCH("유효하지 않은 이메일 토큰 입니다.",
      "INVALID_EMAIL_VERIFICATION_TOKEN", HttpStatus.UNAUTHORIZED),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
