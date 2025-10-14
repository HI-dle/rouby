package com.rouby.assistant.prompt.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PromptErrorCode implements ErrorCode {
  PROMPT_NOT_FOUND("프롬프트를 찾을 수 없습니다.", "PROMPT_NOT_FOUND", HttpStatus.NOT_FOUND),
  INVALID_PROMPT_TYPE("프롬프트 타입이 올바르지 않습니다.", "INVALID_PROMPT_TYPE", HttpStatus.BAD_REQUEST),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
