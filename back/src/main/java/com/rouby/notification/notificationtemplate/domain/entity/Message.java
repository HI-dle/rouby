package com.rouby.notification.notificationtemplate.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Embeddable
@EqualsAndHashCode
public class Message implements Serializable {

  @Column(length = 30, nullable = false)
  private String title;

  @Column(length = 100, nullable = false)
  private String body;

  public static Message of(String title, String body) {
    validateTitle(title);
    validateBody(body);

    return Message.builder()
        .title(title)
        .body(body)
        .build();
  }

  private static void validateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("알림 제목(title)은 비어 있을 수 없습니다.");
    }
    if (title.length() > 30) {
      throw new IllegalArgumentException("알림 제목은 500자를 초과할 수 없습니다.");
    }
  }

  private static void validateBody(String body) {
    if (body == null || body.trim().isEmpty()) {
      throw new IllegalArgumentException("알림 본문(body)은 비어 있을 수 없습니다.");
    }
    if (body.length() > 100) {
      throw new IllegalArgumentException("알림 본문은 1000자를 초과할 수 없습니다.");
    }
  }

  @Builder
  public Message(String title, String body) {
    this.title = title;
    this.body = body;
  }

  protected Message() {
  }

}
