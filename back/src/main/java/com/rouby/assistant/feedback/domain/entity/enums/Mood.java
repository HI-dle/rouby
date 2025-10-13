package com.rouby.assistant.feedback.domain.entity.enums;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
public enum Mood {
  TERRIBLE,
  BAD,
  SOSO,
  GOOD,
  EXCELLENT;

  public static Mood parse(String mood) {
    if (mood.isBlank()) throw new IllegalArgumentException("사용자의 감정이 빈 값이어서는 안 됩니다.");

    try {
      return Mood.valueOf(mood.trim().toUpperCase());
    } catch (Exception e) {
      throw new IllegalArgumentException("사용자의 감정이 적합한 값이 아닙니다: " + mood, e);
    }
  }
}
