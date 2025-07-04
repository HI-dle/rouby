package com.rouby.common.exception;

import com.rouby.common.exception.type.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {

  private final HttpStatus status;

  public CustomException(ErrorCode errorCode) {

    super(errorCode.getMessage());
    this.status = errorCode.getStatus();
  }

  protected CustomException(HttpStatus httpStatus, String message) {
    super(message);
    this.status = httpStatus;
  }

  public static CustomException from(ErrorCode errorCode) {
    return new CustomException(errorCode);
  }

  public static CustomException of(HttpStatus httpStatus, String message) {
    return new CustomException(httpStatus, message);
  }
}
