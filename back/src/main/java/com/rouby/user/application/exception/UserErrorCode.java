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
  //수정은 했는데 맞는 방향인지는 잘 모르겠습니다ㅜ!!
  INVALID_USER("잘못된 사용자 입니다.", "INVALID_USER", HttpStatus.UNAUTHORIZED),
  INVALID_USER_PASSWORD("잘못된 사용자 입니다", "INVALID_USER", HttpStatus.UNAUTHORIZED),
  USER_NOT_FOUND("존재하지 않는 유저입니다.", "USER_NOT_FOUND", HttpStatus.NOT_FOUND),
  INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", "INVALID_PASSWORD", HttpStatus.FORBIDDEN);

  private final String message;
  private final String code;
  private final HttpStatus status;
}
