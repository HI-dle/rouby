package com.rouby.assistant.prompt.domain.entity.enums;

public enum PromptType {
  ROUTINE,
  FEEDBACK,
  BRIEFING,
  ;

  public static PromptType parse(String type) {

    if (type == null) throw new IllegalArgumentException("PromptType은 null 일 수 없습니다.");

    try {
      return PromptType.valueOf(type.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("입력된 PromptType 값이 적절한 값이 아닙니다.");
    }
  }
}
