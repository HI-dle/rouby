package com.rouby.notification.notificationlog.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class NotificationMessage implements Serializable {

  @Column(columnDefinition = "TEXT", name = "message", nullable = false)
  private String message;

  public static NotificationMessage of(String message) {
    if (!StringUtils.hasText(message)) {
      throw new IllegalArgumentException("메시지는 비어 있을 수 없습니다.");
    }
    return new NotificationMessage(message);
  }

  protected NotificationMessage() {}

  private NotificationMessage(String message) {
    this.message = message;
  }
}
