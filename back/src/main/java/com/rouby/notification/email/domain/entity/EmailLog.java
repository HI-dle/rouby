package com.rouby.notification.email.domain.entity;

import com.rouby.common.jpa.LogBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
    name = "email_log",
    indexes = {
        @Index(name = "idx_email_created_at", columnList = "email_address, created_at")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailLog extends LogBaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Embedded
  private EmailContent content;

  @Embedded
  private EmailAddress address;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SendStatus status;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private EmailType type;

  public static EmailLog sent(EmailContent content, EmailAddress address, EmailType type) {
    return EmailLog.builder()
        .content(content)
        .address(address)
        .status(SendStatus.SENT)
        .type(type).build();
  }

  public static EmailLog failed(EmailContent content, EmailAddress address, EmailType type) {
    return EmailLog.builder()
        .content(content)
        .address(address)
        .status(SendStatus.FAILED)
        .type(type).build();
  }

  @Builder
  private EmailLog(EmailContent content, EmailAddress address,
      SendStatus status, EmailType type) {
    this.content = content;
    this.address = address;
    this.status = status;
    this.type = type;
  }
}
