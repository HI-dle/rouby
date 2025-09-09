package com.rouby.user.device.application.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserDeviceErrorCode implements ErrorCode {
  NOT_FOUND_USER_DEVICE("조건에 해당하는 유저 디바이스 정보가 존재하지 않습니다.", "NOT_FOUND_USER_DEVICE", HttpStatus.NOT_FOUND),
  USER_ID_LIST_EMPTY("유저 ID 리스트가 비어 있습니다.", "USER_ID_LIST_EMPTY", HttpStatus.BAD_REQUEST),
  ;

  private final String message;
  private final String code;
  private final HttpStatus status;
}
