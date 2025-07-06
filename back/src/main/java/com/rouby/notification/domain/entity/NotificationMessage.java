package com.rouby.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
public class NotificationMessage implements Serializable {

  @Column(columnDefinition = "TEXT", name = "message", nullable = false)
  private String value;

  public static NotificationMessage of(String value) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalArgumentException("메시지는 비어 있을 수 없습니다.");
    }
    return new NotificationMessage(value);
  }

  protected NotificationMessage() {}

  private NotificationMessage(String value) {
    this.value = value;
  }
}
