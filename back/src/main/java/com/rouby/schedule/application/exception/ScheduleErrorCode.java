package com.rouby.schedule.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ScheduleErrorCode implements ErrorCode {
  SCHEDULE_INVALID_REQUEST(
      "유효하지 않은 요청 정보입니다.",
      "SCHEDULE_INVALID_REQUEST",
      HttpStatus.UNPROCESSABLE_ENTITY),


  SCHEDULE_NOT_FOUND(
      "존재하지 않는 일정입니다.",
          "SCHEDULE_NOT_FOUND",
      HttpStatus.NOT_FOUND),
  ;
  private final String message;
  private final String code;
  private final HttpStatus status;
}
