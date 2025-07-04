package com.rouby.user.domain.entity;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.user.domain.entity.vo.AuthProvider;
import com.rouby.user.domain.entity.vo.CommunicationTone;
import com.rouby.user.domain.entity.vo.DailyActiveTime;
import com.rouby.user.domain.entity.vo.InterestKeywords;
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
  private List<NotificationSetting> notificationSettings = new ArrayList<>();

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AuthProvider authProvider;

  private LocalDateTime lastActivatedAt;

  protected User() {
  }

  private User(
      String email,
      String password,
      String nickname,
      DailyActiveTime dailyActiveTime,
      InterestKeywords interestKeyword,
      CommunicationTone communicationTone,
      List<NotificationSetting> notificationSettings,
      AuthProvider authProvider,
      LocalDateTime lastActivatedAt
  ) {
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.dailyActiveTime = dailyActiveTime;
    this.interestKeyword = interestKeyword;
    this.communicationTone = communicationTone;
    this.notificationSettings = notificationSettings;
    this.authProvider = authProvider;
    this.lastActivatedAt = lastActivatedAt;
  }
}
