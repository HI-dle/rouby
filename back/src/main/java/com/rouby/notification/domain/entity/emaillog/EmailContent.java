package com.rouby.notification.domain.entity.emaillog;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class EmailContent implements Serializable {

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private String value;

  public static EmailContent of(String value) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalArgumentException("이메일 내용은 비어 있을 수 없습니다.");
    }
    return new EmailContent(value);
  }

  private EmailContent(String value) {
    this.value = value;
  }

  protected EmailContent() {}
}
