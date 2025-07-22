package com.rouby.routine.routine_task.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoutineTaskErrorCode implements ErrorCode {
  ROUTINE_TASK_NOT_FOUND("존재하지 않는 루틴 태스크입니다.", "ROUTINE_TASK_NOT_FOUND", HttpStatus.NOT_FOUND),
  ROUTINE_TASK_ACCESS_DENIED("루틴 태스크에 접근할 권한이 없습니다.", "ROUTINE_TASK_ACCESS_DENIED", HttpStatus.FORBIDDEN),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
