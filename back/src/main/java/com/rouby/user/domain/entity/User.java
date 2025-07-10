package com.rouby.user.domain.entity;

import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  private String password;

  @Column(length = 20)
  private String nickname;

  @Embedded
  private DailyActiveTime dailyActiveTime;

  @Embedded
  private InterestKeywords interestKeywords;

  @Embedded
  private CommunicationTone communicationTone;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<NotificationSetting> notificationSettings;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  private LocalDateTime lastActivatedAt;

  public void addNotificationSetting(NotificationType type, boolean isEnabled) {
    NotificationSetting setting = NotificationSetting.builder()
        .user(this)
        .notificationType(type)
        .isEnabled(isEnabled)
        .build();

    this.notificationSettings.add(setting);
  }

  @Builder
  private User(
      String email,
      String password,
      String nickname,
      AuthProvider authProvider,
      UserRole role,
      LocalDateTime lastActivatedAt
  ) {
    if (email == null) {
      throw new IllegalArgumentException("이메일은 null일 수 없습니다.");
    }
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.dailyActiveTime = DailyActiveTime.defaultTime();
    this.interestKeywords = InterestKeywords.empty();
    this.communicationTone = CommunicationTone.empty();
    this.notificationSettings = new HashSet<>();
    this.authProvider = authProvider == null ? AuthProvider.DEFAULT : authProvider;
    this.role = role == null ? UserRole.USER : role;
    this.lastActivatedAt = lastActivatedAt;
  }

  protected User() {
  }

  public void updatePassword(String newPassword) {
    this.password = newPassword;
  }
}
