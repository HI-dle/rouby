package com.rouby.assistant.briefing.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BriefingErrorCode implements ErrorCode {
  BRIEFING_NOT_FOUND("존재하지 않는 브리핑입니다.", "BRIEFING_NOT_FOUND", HttpStatus.NOT_FOUND),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
