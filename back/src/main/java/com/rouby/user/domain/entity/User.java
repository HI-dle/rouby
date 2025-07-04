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
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "users")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  private String password;

  @Column(length = 20)
  private String nickname;

  @Embedded
  private DailyActiveTime dailyActiveTime;

  @Embedded
  private InterestKeywords interestKeyword;

  @Embedded
  private CommunicationTone communicationTone;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NotificationSetting> notificationSettings;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

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
      LocalDateTime lastActivatedAt
  ) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.dailyActiveTime = DailyActiveTime.defaultTime();
    this.interestKeyword = InterestKeywords.empty();
    this.communicationTone = CommunicationTone.empty();
    this.notificationSettings = new ArrayList<>();
    this.authProvider = authProvider;
    this.lastActivatedAt = lastActivatedAt;
  }

  protected User() {
  }
}
