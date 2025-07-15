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
public class EmailContent implements Serializable {

  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  private String content;

  public static EmailContent of(String content) {
    if (!StringUtils.hasText(content)) {
      throw new IllegalArgumentException("이메일 내용은 비어 있을 수 없습니다.");
    }
    return new EmailContent(content);
  }

  private EmailContent(String content) {
    this.content = content;
  }

  protected EmailContent() {}
}
