package com.rouby.assistant.feedback.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FeedbackErrorCode implements ErrorCode {

  INVALID_FEEDBACK_ID("피드백 아이디가 유효하지 않습니다.", "INVALID_FEEDBACK_ID", HttpStatus.BAD_REQUEST),

  EXCEEDED_DAILY_FEEDBACK_USAGE("피드백의 당일 사용량을 모두 초과하였습니다.", "EXCEEDED_DAILY_FEEDBACK_USAGE", HttpStatus.TOO_MANY_REQUESTS),
  CONCURRENT_REQUEST_FAILED("너무 빠른 시간 내에 피드백을 동시 요청할 수 없습니다.", "CONCURRENT_FEEDBACK_REQUEST_FAILED", HttpStatus.TOO_MANY_REQUESTS),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;

}
