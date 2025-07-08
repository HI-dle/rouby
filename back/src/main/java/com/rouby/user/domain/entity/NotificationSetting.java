package com.rouby.user.domain.entity;

import com.rouby.common.jpa.BaseEntity;
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
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class NotificationSetting extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private NotificationType notificationType;

  @Column(nullable = false)
  private boolean isEnabled;

  public void enable() {
    this.isEnabled = true;
  }

  public void disable() {
    this.isEnabled = false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NotificationSetting that = (NotificationSetting) o;
    return Objects.equals(user, that.user) && notificationType == that.notificationType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, notificationType);
  }

  public static NotificationSetting createDefault(User user, NotificationType type){
    return NotificationSetting.builder()
        .user(user)
        .notificationType(type)
        .isEnabled(false)
        .build();
  }

  @Builder
  private NotificationSetting(User user, NotificationType notificationType, boolean isEnabled) {
    this.user = user;
    this.notificationType = notificationType;
    this.isEnabled = isEnabled;
  }

  protected NotificationSetting() {
  }
}

