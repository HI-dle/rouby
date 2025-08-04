package com.rouby.assistant.prompt.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PromptErrorCode implements ErrorCode {
  PROMPT_NOT_FOUNT("프롬프트를 찾을 수 없습니다.", "PROMPT_NOT_FOUNT", HttpStatus.NOT_FOUND),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
