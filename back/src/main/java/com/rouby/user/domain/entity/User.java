package com.rouby.user.domain.entity;

import static com.rouby.user.domain.entity.OnboardingState.COMPLETED;
import static com.rouby.user.domain.entity.OnboardingState.ROUBY_SETTING_BEFORE;

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
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
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
  private HealthStatusKeywords healthStatusKeywords;

  @Embedded
  private ProfileKeywords profileKeywords;

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
    if (plainPassword == null || plainPassword.isBlank()) {
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

  public void completeUserInfoSetting() {
    if (!this.onboardingState.canTransitTo(ROUBY_SETTING_BEFORE)) {
      throw new IllegalStateException(
          "유저 정보 설정을 완료할 수 없는 상태입니다. 현재 상태: " + this.onboardingState);
    }

    validateUserInfoRequirement();
    this.onboardingState = ROUBY_SETTING_BEFORE;
  }

  private void validateUserInfoRequirement() {
    if (nickname == null || nickname.isBlank()) {
      throw new IllegalStateException("닉네임은 필수입니다.");
    }
    if (healthStatusKeywords.isEmpty()) {
      throw new IllegalStateException("유저의 건강 상태 키워드는 비어있을 수 없습니다.");
    }
    if (profileKeywords.isEmpty()) {
      throw new IllegalStateException("유저의 프로필 키워드는 비어있을 수 없습니다.");
    }
    if (dailyActiveTime.isNull()) {
      throw new IllegalStateException("유저의 활동 시간은 null 일 수 없습니다.");
    }
  }

  public void completeRoubySetting() {
    if (!this.onboardingState.canTransitTo(COMPLETED)) {
      throw new IllegalStateException(
          "루비 정보 설정을 완료할 수 없는 상태입니다. 현재 상태: " + this.onboardingState);
    }

    validateRoubySettingRequirement();
    this.onboardingState = COMPLETED;
  }

  private void validateRoubySettingRequirement() {
    if (communicationTone.isEmpty()) {
      throw new IllegalStateException("루미 말투는 비어있을 수 없습니다.");
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
    this.healthStatusKeywords = HealthStatusKeywords.empty();
    this.profileKeywords = ProfileKeywords.empty();
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

  public Set<String> getCommunicationToneValues() {
    if (communicationTone == null) return Collections.emptySet();
    return communicationTone.getRoubyCommunicationTone();
  }

  protected User() {
    this.notificationSettings = new HashSet<>();
  }

  public void updatePassword(UserPasswordEncoder passwordEncoder, String newPassword) {
    validatePassword(newPassword);
    this.password = passwordEncoder.encode(newPassword);
  }

  public void updateRoubySettings(
      CommunicationTone communicationTone,
      Set<NotificationSetting> notificationSettings) {
    this.communicationTone = communicationTone;
    this.notificationSettings.clear();
    this.notificationSettings.addAll(notificationSettings);
  }

  public void updateUserInfo(
      String nickname,
      Set<String> healthStatusKeywords,
      Set<String> profileKeywords,
      LocalTime dailyStartTime,
      LocalTime dailyEndTime
  ) {
    this.nickname = nickname;
    this.healthStatusKeywords = HealthStatusKeywords.of(healthStatusKeywords);
    this.profileKeywords = ProfileKeywords.of(profileKeywords);
    this.dailyActiveTime = DailyActiveTime.of(dailyStartTime, dailyEndTime);
  }

  public void delete(Long userId) {
    this.password = null;
    this.nickname = null;
    this.dailyActiveTime = null;
    this.healthStatusKeywords = null;
    this.profileKeywords = null;
    this.communicationTone = null;
    this.notificationSettings.clear();
    this.authProvider = AuthProvider.DEFAULT;
    this.lastActivatedAt = null;

    super.delete(userId);
  }
}
