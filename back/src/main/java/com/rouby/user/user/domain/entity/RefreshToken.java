package com.rouby.user.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "refresh_token")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  @Builder
  private RefreshToken(User user, String token, LocalDateTime expiredAt) {
    this.user = user;
    this.token = token;
    this.expiredAt = expiredAt;
  }

  public static RefreshToken create(User user, String token) {
    return RefreshToken.builder()
        .user(user)
        .token(token)
        .expiredAt(LocalDateTime.now().plusDays(30))
        .build();
  }

  protected RefreshToken() {
  }

  public boolean isExpired() {
    return expiredAt.isBefore(LocalDateTime.now());
  }
}
