package com.rouby.notification.domain.entity.emaillog;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "email_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog extends LogBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Embedded
  private EmailContent content;

  @Embedded
  private EmailAddress emailAddress;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SendStatus status;

  @Builder
  private EmailLog(Long userId, EmailContent content, EmailAddress emailAddress,
      SendStatus status) {
    this.userId = userId;
    this.content = content;
    this.emailAddress = emailAddress;
    this.status = status;
  }
}
