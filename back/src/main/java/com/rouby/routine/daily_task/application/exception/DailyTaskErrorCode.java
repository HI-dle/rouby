package com.rouby.routine.daily_task.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DailyTaskErrorCode implements ErrorCode {
  DUPLICATE_DAILY_TASK("동일한 날짜의 데일리 태스크가 이미 존재합니다.", "DUPLICATE_DAILY_TASK", HttpStatus.CONFLICT),
  DAILY_TASK_NOT_FOUND("존재하지 않는 데일리 태스크입니다.", "DAILY_TASK_NOT_FOUND", HttpStatus.NOT_FOUND),

  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
