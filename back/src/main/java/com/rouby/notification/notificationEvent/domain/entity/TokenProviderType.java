package com.rouby.notification.notificationEvent.domain.entity;

public enum TokenProviderType {
  FCM,
  APNs,
  ;

  public static TokenProviderType parse(String tokenProvider) {

    if (tokenProvider == null || tokenProvider.isBlank()) {
      throw new IllegalArgumentException("디바이스 토큰 제공자가 공백일 수 없습니다.");
    }
    try {
      return TokenProviderType.valueOf(tokenProvider.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("디바아스 토큰 제공자 정보가 유효한 값이 아닙니다. : " + tokenProvider, e);
    }
  }
}
