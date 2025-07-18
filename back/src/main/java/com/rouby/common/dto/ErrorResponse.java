package com.rouby.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.rouby.common.exception.CustomException;
import java.util.List;

public record ErrorResponse(
    String message,
    String code,
    @JsonInclude(JsonInclude.Include.NON_NULL) List<ErrorField> errors
) {

  public static ErrorResponse from(Throwable ex) {
    return new ErrorResponse(ex.getMessage(), null, null);
  }

  public static ErrorResponse from(Exception ex) {
    return new ErrorResponse(ex.getMessage(), null, null);
  }

  public static ErrorResponse from(CustomException ex) {
    return new ErrorResponse(ex.getMessage(), ex.getCode(), null);
  }

  public static ErrorResponse of(String message, String code) {
    return new ErrorResponse(message, code, null);
  }

  public static ErrorResponse of(String message, List<ErrorField> errors) {
    return new ErrorResponse(message, null, errors);
  }

  public static ErrorResponse of(String message, String code, List<ErrorField> errors) {
    return new ErrorResponse(message, code, errors);
  }

  public record ErrorField(Object value, String message) {
  }
}