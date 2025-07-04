package com.rouby.common.exception.type;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String getMessage();
  HttpStatus getStatus();
}
