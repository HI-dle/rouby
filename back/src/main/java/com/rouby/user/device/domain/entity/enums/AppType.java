package com.rouby.user.device.domain.entity.enums;

public enum AppType {
  UNDEFINED,
  ANDROID,
  IOS,
  WEB,
  ;

  public static AppType parse(String appType) {

    if (appType == null || appType.isBlank()) {
      return UNDEFINED;
    }

    try {
      return valueOf(appType.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("지원되지 않는 어플리케이션 타입입니다: " + appType, e);
    }
  }
}
