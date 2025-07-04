package com.rouby.user.domain.entity;

import com.rouby.user.domain.entity.vo.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class NotificationSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  @Column(nullable = false)
  private boolean isEnabled;

  protected NotificationSetting() {}

  private NotificationSetting(User user, NotificationType notificationType, boolean isEnabled) {
    this.user = user;
    this.notificationType = notificationType;
    this.isEnabled = isEnabled;
  }
}

