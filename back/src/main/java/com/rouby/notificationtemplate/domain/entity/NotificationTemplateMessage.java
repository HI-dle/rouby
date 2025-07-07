package com.rouby.notificationtemplate.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class NotificationTemplateMessage {

  @Column(columnDefinition = "TEXT", name = "message", nullable = false)
  private String value;

  public static NotificationTemplateMessage of(String value) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalArgumentException("알림 템플릿 메시지는 비어 있을 수 없습니다.");
    }
    return new NotificationTemplateMessage(value);
  }

  private NotificationTemplateMessage(String value) {
    this.value = value;
  }

  protected NotificationTemplateMessage() {}

}
