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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

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

  @Column(nullable = false)
  private LocalDateTime dueAt;

  @Column
  private LocalDateTime retryAt;

  @Column
  private LocalDateTime sentAt;

  @Column(nullable = false)
  private Integer attempt;

  @Column
  private LocalDateTime leaseUntil;

  @Column
  private String workerId;

  @LastModifiedDate
  @Column
  private LocalDateTime updatedAt;

  @Builder
  private NotificationEvent(Long userId,
      DeviceTokenInfo deviceTokenInfo, NotificationMessage message,
      NotificationType type, LocalDateTime dueAt, SendStatus status) {

    this.userId = userId;
    this.deviceTokenInfo = deviceTokenInfo;
    this.message = message;
    this.type = type;
    this.dueAt = dueAt;
    this.status = status;
    if (status == null) this.status = SendStatus.PENDING;

    this.attempt = 0;
  }

  public void updateMessageUrl(String url) {
    this.message = this.message.withUrl(url);  // 새 메시지를 통째로 교체
  }
}
