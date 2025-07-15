package com.rouby.notification.email.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class EmailAddress implements Serializable {

  @Column(name = "email_address", nullable = false)
  private String emailAddress;

  public static EmailAddress of(String emilAddress) {
    if (!StringUtils.hasText(emilAddress)) {
      throw new IllegalArgumentException("이메일 주소는 비어 있을 수 없습니다.");
    }
    return new EmailAddress(emilAddress);
  }

  private EmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  protected EmailAddress() {}

}
