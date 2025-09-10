package com.rouby.notification.notificationEvent.domain.entity;


import com.rouby.common.jpa.LogBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "notification_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEvent extends LogBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Embedded
  private DeviceTokenInfo deviceTokenInfo;

  @Embedded
  private NotificationMessage message;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SendStatus status;

  @Builder
  private NotificationEvent(Long userId,
      DeviceTokenInfo deviceTokenInfo, NotificationMessage message, NotificationType type) {

    this.userId = userId;
    this.deviceTokenInfo = deviceTokenInfo;
    this.message = message;
    this.type = type;
    this.status = SendStatus.READY;
  }

  public void updateMessageUrl(String url) {
    this.message = this.message.withUrl(url);  // 새 메시지를 통째로 교체
  }
}
