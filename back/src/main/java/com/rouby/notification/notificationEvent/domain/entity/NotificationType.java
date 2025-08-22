package com.rouby.notification.notificationEvent.domain.entity;

public enum NotificationType {
  SCHEDULE,
  ROUTINE,
  BRIEFING,
  ;

  public static NotificationType parse(String notificationType) {

    if (notificationType == null || notificationType.isBlank())
      throw new IllegalArgumentException("알림 타입은 널이거나 빈 값일 수 없습니다.");

    try {
      return NotificationType.valueOf(notificationType.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("지원되지 않는 알림 타입입니다. : " + notificationType, e);
    }
  }
}
