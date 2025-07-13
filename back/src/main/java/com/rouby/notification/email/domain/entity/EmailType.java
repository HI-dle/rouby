package com.rouby.notification.email.domain.entity;

public enum EmailType {
  VERIFICATION,
  RESET_PASSWORD
  ;

  public static EmailType of(String name) {
    try {
      return valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("존재하지 않는 타입입니다.");
    }
  }
}
