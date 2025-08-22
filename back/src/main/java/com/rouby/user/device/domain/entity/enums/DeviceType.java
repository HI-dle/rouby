package com.rouby.user.device.domain.entity.enums;

public enum DeviceType {
  UNDEFINED,
  DESKTOP,
  MOBILE,
  TABLET,
  TV,
  EMBEDDED,
  ;

  public static DeviceType parse(String deviceType) {

    if (deviceType == null || deviceType.isBlank()) {
      return UNDEFINED;
    }
    try {
      return DeviceType.valueOf(deviceType.trim().toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("지원되지 않는 디바이스 값입니다: " + deviceType, e);
    }
  }
}
