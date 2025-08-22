package com.rouby.notification.notificationtemplate.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class Message implements Serializable {

  @Column(columnDefinition = "TEXT", name = "message", nullable = false)
  private String value;

  public static Message of(String value) {
    if (!StringUtils.hasText(value)) {
      throw new IllegalArgumentException("알림 템플릿 메시지는 비어 있을 수 없습니다.");
    }
    return new Message(value);
  }

  private Message(String value) {
    this.value = value;
  }

  protected Message() {}

}
