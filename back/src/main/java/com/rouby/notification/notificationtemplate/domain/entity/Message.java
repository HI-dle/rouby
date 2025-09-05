package com.rouby.notification.notificationtemplate.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
@Embeddable
@EqualsAndHashCode
public class Message implements Serializable {

  @Column(length = 500, nullable = false)
  private String title;

  @Column(length = 1000, nullable = false)
  private String body;

  public static Message of(String title, String body) {
    if (!StringUtils.hasText(title) || !StringUtils.hasText(body)) {
      throw new IllegalArgumentException("알림 템플릿 메시지는 비어 있을 수 없습니다.");
    }
    return Message.builder()
        .title(title)
        .body(body)
        .build();
  }

  @Builder
  public Message(String title, String body) {
    this.title = title;
    this.body = body;
  }

  protected Message() {
  }

}
