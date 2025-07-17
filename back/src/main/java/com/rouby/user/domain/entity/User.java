package com.rouby.user.domain.entity;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.user.domain.service.UserPasswordEncoder;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
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
  private CurrentStatusKeywords currentStatusKeywords;

  @Embedded
  private HealthStatusKeywords healthStatusKeywords;

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

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OnboardingState onboardingState;

  private LocalDateTime lastActivatedAt;

  public static User create(
      String email, String plainPassword, UserPasswordEncoder passwordEncoder) {
    validateEmail(email);
    validatePassword(plainPassword);

    return User.builder()
        .email(email)
        .password(passwordEncoder.encode(plainPassword))
        .nickname(email.substring(0, email.indexOf("@")))
        .role(UserRole.USER)
        .onboardingState(OnboardingState.USER_INFO_SETTING_BEFORE)
        .build();
  }

  private static void validateEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      throw new IllegalArgumentException("이메일은 필수입니다.");
    }
    if (email.length() > 100) {
      throw new IllegalArgumentException("이메일은 100자를 초과할 수 없습니다.");
    }
    if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
      throw new IllegalArgumentException("유효하지 않은 이메일 형식입니다.");
    }
  }

  private static void validatePassword(String plainPassword) {
    if (plainPassword==null || plainPassword.isBlank()) {
      throw new IllegalArgumentException("비밀번호는 필수입니다.");
    }
    if (plainPassword.length() < 8 || plainPassword.length() > 32) {
      throw new IllegalArgumentException("비밀번호는 8~32자 사이여야 합니다.");
    }
    if (!plainPassword.matches(
        "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)[A-Za-z\\d\\W_]{8,32}$")) {
      throw new IllegalArgumentException(
          "비밀번호는 영문/숫자/특수문자 중 2가지 이상 조합이어야 합니다.");
    }
  }

  @Builder
  private User(
      String email,
      String password,
      String nickname,
      AuthProvider authProvider,
      UserRole role,
      LocalDateTime lastActivatedAt,
      OnboardingState onboardingState
  ) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.dailyActiveTime = DailyActiveTime.defaultTime();
    this.currentStatusKeywords = CurrentStatusKeywords.empty();
    this.healthStatusKeywords = HealthStatusKeywords.empty();
    this.interestKeywords = InterestKeywords.empty();
    this.communicationTone = CommunicationTone.empty();
    this.notificationSettings = createDefaultNotificationSettings();
    this.authProvider = authProvider == null ? AuthProvider.DEFAULT : authProvider;
    this.role = role == null ? UserRole.USER : role;
    this.lastActivatedAt = lastActivatedAt;
    this.onboardingState = onboardingState;
  }

  private Set<NotificationSetting> createDefaultNotificationSettings() {
    return Arrays.stream(NotificationType.values())
        .map(type -> NotificationSetting.createDefault(this, type))
        .collect(Collectors.toSet());
  }


  protected User() {
    this.notificationSettings = new HashSet<>();
  }

  public void updatePassword(UserPasswordEncoder passwordEncoder, String newPassword) {
    validatePassword(newPassword);
    this.password = passwordEncoder.encode(newPassword);
  }
}
