package com.rouby.notification.domain.entity.emaillog;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class EmailSent implements Serializable {

  @Column(nullable = false)
  private String value;

  public static EmailSent of(String value) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalArgumentException("이메일 주소는 비어 있을 수 없습니다.");
    }
    return new EmailSent(value);
  }

  private EmailSent(String value) {
    this.value = value;
  }

  protected EmailSent() {}

}
