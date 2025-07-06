package com.rouby.notification.domain.entity;


import com.rouby.common.jpa.LogBaseEntity;
import com.rouby.user.domain.entity.NotificationType;
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
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends LogBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Embedded
  private NotificationMessage message;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType type;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SendStatus status;

  @Builder
  private Notification(Long userId, NotificationMessage message, NotificationType type,
      SendStatus status) {
    this.userId = userId;
    this.message = message;
    this.type = type;
    this.status = status;
  }
}
